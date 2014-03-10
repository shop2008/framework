package com.wxxr.mobile.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RootLayout extends ViewGroup {

	private Context context;
	private LayoutInflater layoutInflater;
	Map<String, String> overMaps = new HashMap<String, String>();
	Map<String, View> ParamsMaps = new HashMap<String, View>();
	private int headerIhc = -1;
	private int contentIhc = -1;
	private int footerIhc = -1;

	public RootLayout(Context context) {
		this(context, null);
		this.context = context;
		// LayoutInflater.from(context).inflate(R.layout.trading_page_layout,
		// this);
	}

	public RootLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
		// LayoutInflater.from(context).inflate(R.layout.trading_page_layout,
		// this);
	}

	public RootLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		// LayoutInflater.from(context).inflate(R.layout.trading_page_layout,
		// this);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// LayoutInflater.from(context)
		// .inflate(R.layout.trading_page_layout, this);
		// layoutInflater = (LayoutInflater) context
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View view = layoutInflater.inflate(R.layout.trading_page_layout,
		// null);
		// ViewGroup.MarginLayoutParams params = new
		// ViewGroup.MarginLayoutParams(
		// ViewGroup.MarginLayoutParams.MATCH_PARENT,
		// ViewGroup.MarginLayoutParams.MATCH_PARENT);
		// ViewGroup.MarginLayoutParams p =
		// (ViewGroup.MarginLayoutParams)view.getLayoutParams();
		// params.topMargin = p.topMargin;
		// params.bottomMargin = p.bottomMargin;
		// params.leftMargin = p.leftMargin;
		// params.rightMargin = p.rightMargin;
		// ((ViewGroup) findViewById(R.id.root)).addView(view, params);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode == MeasureSpec.UNSPECIFIED) {
			throw new IllegalStateException("RootLayout can not be used with "
					+ "measure spec mode=UNSPECIFIED");
		}
		View content = null;
		int headerHeight = 0;
		int footerHeight = 0;
		int childCount = getChildCount();
		int specSize_Widht = MeasureSpec.getSize(widthMeasureSpec);
		int specSize_Heigth = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(specSize_Widht, specSize_Heigth);

		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			int identity = System.identityHashCode(child);
			if (child.getVisibility() == View.GONE) {
				continue;
			}
			if (identity != contentIhc)
				this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
			else
				content = child;
			if (identity == headerIhc) {
				headerHeight = child.getMeasuredHeight();
			} else if (identity == footerIhc) {
				footerHeight = child.getMeasuredHeight();
			}
		}
		this.measureChild(content, widthMeasureSpec, heightMeasureSpec
				- headerHeight - footerHeight);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int left = l + getPaddingLeft();
		int right = r - getPaddingRight();
		int top = t + getPaddingTop();
		int bottom = b - getPaddingBottom();
		// content参数
		View content = null;
		int contentTop = top;
		int contentBottom = bottom;

		ParamsMaps.clear();

		List<String> keyList = mapKey2List(overMaps);
		List<String> valueList = mapValue2List(overMaps);
		int childCount = getChildCount();
		// 布局非遮盖view并保存params
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			int identity = System.identityHashCode(child);
			if (keyList.contains(identity + "")
					|| child.getVisibility() == View.GONE) {
				continue;
			}
			if (identity == headerIhc) {// 布局headerView
				ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child
						.getLayoutParams();
				for (String id : valueList) {
					View v = child.findViewById(Integer.valueOf(id));
					if (v != null) {
						ParamsMaps.put(id, v);
					}
				}
				int childWidth = child.getMeasuredWidth();
				int childHeight = child.getMeasuredHeight();
				contentTop = top + layoutParams.topMargin + childHeight
						+ layoutParams.bottomMargin;

				child.layout(left + layoutParams.leftMargin, top
						+ layoutParams.topMargin, left
						+ layoutParams.leftMargin + childWidth,
						layoutParams.topMargin + childHeight);
			} else if (identity == contentIhc) {// 记录ContentView
				content = child;
			} else if (identity == footerIhc) {// 布局FooterView
				ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child
						.getLayoutParams();
				for (String id : valueList) {
					View v = child.findViewById(Integer.valueOf(id));
					if (v != null) {
						ParamsMaps.put(id, v);
					}
				}
				int childWidth = child.getMeasuredWidth();
				int childHeight = child.getMeasuredHeight();
				contentBottom = bottom - layoutParams.bottomMargin
						- childHeight - layoutParams.topMargin;

				child.layout(left + layoutParams.leftMargin, bottom
						- layoutParams.bottomMargin - childHeight, left
						+ layoutParams.leftMargin + childWidth, bottom
						- layoutParams.bottomMargin);
			} else {
				ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child
						.getLayoutParams();
				for (String id : valueList) {
					View v = child.findViewById(Integer.valueOf(id));
					if (v != null) {
						ParamsMaps.put(id, v);
					}
				}
				int childWidth = child.getMeasuredWidth();
				int childHeight = child.getMeasuredHeight();
				child.layout(left + layoutParams.leftMargin, top
						+ layoutParams.topMargin, left
						+ layoutParams.leftMargin + childWidth,
						layoutParams.topMargin + childHeight);
			}
		}
		// 布局ContentView
		if (content != null && contentIhc != -1) {
			ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) content
					.getLayoutParams();
			for (String id : valueList) {
				View v = content.findViewById(Integer.valueOf(id));
				if (v != null) {
					ParamsMaps.put(id, v);
				}
			}
			int contentWidth = content.getMeasuredWidth();
			int contentHeight = content.getMeasuredHeight();
			content.layout(left + layoutParams.leftMargin, contentTop
					+ layoutParams.topMargin, left + layoutParams.leftMargin
					+ contentWidth, contentBottom - layoutParams.bottomMargin);
		}
		// 根据params布局遮盖view
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			String identity = System.identityHashCode(child) + "";
			if (keyList.contains(identity)) {
				String id = overMaps.get(identity);
				if (child.getVisibility() != View.GONE) {
					View v = ParamsMaps.get(id);
					if (v != null) {
						int childWidth = v.getMeasuredWidth();
						int childHeight = v.getMeasuredHeight();
						ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
								childWidth, childHeight);
						int[] lt = getViewLTPoint(v);
						int[] rb = getViewRBPoint(v);

						int topMargin = lt[1];
						int leftMargin = lt[0];
						int rightMargin = 0;
						int bottomMargin = 0;

						int[] ltp = getViewLTPoint(this);
						int[] rbp = getViewRBPoint(this);
						topMargin = lt[1] - ltp[1];
						leftMargin = lt[0] - ltp[0];
						rightMargin = rbp[0] - rb[0];
						bottomMargin = rbp[1] - rb[1];

						// params.topMargin = topMargin + v.getPaddingTop();
						// params.leftMargin = leftMargin + v.getPaddingLeft();
						// params.rightMargin = rightMargin +
						// v.getPaddingRight();
						// params.bottomMargin = bottomMargin
						// + v.getPaddingBottom();
						// child.setLayoutParams(params);
						//
						// child.layout(
						// left + params.leftMargin,
						// top + params.topMargin,
						// left + params.leftMargin + childWidth
						// - v.getPaddingRight()
						// - v.getPaddingLeft(),
						// top + params.topMargin + childHeight
						// - v.getPaddingBottom()
						// - v.getPaddingTop());

						params.topMargin = topMargin;
						params.leftMargin = leftMargin;
						child.setLayoutParams(params);

						child.layout(left + params.leftMargin, top
								+ params.topMargin, left + params.leftMargin
								+ childWidth, top + params.topMargin
								+ childHeight);
					}
				}
			}
		}
	}

	int measureNullChild(int childIndex) {
		return 0;
	}

	int getChildrenSkipCount(View child, int index) {
		return 0;
	}

	int getNextLocationOffset(View child) {
		return 0;
	}

	void measureChildBeforeLayout(View child, int childIndex,
			int widthMeasureSpec, int totalWidth, int heightMeasureSpec,
			int totalHeight) {
		measureChildWithMargins(child, widthMeasureSpec, totalWidth,
				heightMeasureSpec, totalHeight);
	}

	public static List mapValue2List(Map map) {
		List list = new ArrayList();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			list.add(entry.getValue());
		}
		return list;
	}

	public static List mapKey2List(Map map) {
		List list = new ArrayList();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			list.add(entry.getKey());
		}
		return list;
	}

	public static int[] getViewLTPoint(View view) {
		if (view == null)
			return null;
		int[] location = new int[2];
		view.getLocationOnScreen(location);

		return location;
	}

	public static int[] getViewRBPoint(View view) {
		if (view == null)
			return null;
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		location[0] += view.getWidth();
		location[1] += view.getHeight();

		return location;
	}

	public void addOverrideView(View view, int id) {
		if (overMaps.containsKey(System.identityHashCode(view) + ""))
			throw new IllegalStateException(
					"The specified child already has a parent. You must call removeView() on the child's parent first.");
		overMaps.put(System.identityHashCode(view) + "", id + "");
		addView(view);
	}

	public void addHeaderView(View view) {
		if (headerIhc != -1)
			throw new IllegalStateException(
					"The Header View already has a parent. You must call removeView() on the child's parent first.");
		headerIhc = System.identityHashCode(view);
		addView(view);
	}

	public void addContentView(View view) {
		if (contentIhc != -1)
			throw new IllegalStateException(
					"The Content View already has a parent. You must call removeView() on the child's parent first.");
		contentIhc = System.identityHashCode(view);
		ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(view, params);
	}

	public void addFooterView(View view) {
		if (footerIhc != -1)
			throw new IllegalStateException(
					"The Footer View already has a parent. You must call removeView() on the child's parent first.");
		footerIhc = System.identityHashCode(view);
		addView(view);
	}

	@Override
	public void removeView(View view) {
		int ihc = System.identityHashCode(view);
		if (overMaps.containsKey(ihc + "")) {
			overMaps.remove(ihc + "");
		}
		if (ihc == headerIhc) {
			headerIhc = -1;
		} else if (ihc == contentIhc) {
			contentIhc = -1;
		} else if (ihc == footerIhc) {
			footerIhc = -1;
		}
		super.removeView(view);
	}

	@Override
	public void removeAllViews() {
		super.removeAllViews();
		overMaps.clear();
		ParamsMaps.clear();
		headerIhc = -1;
		contentIhc = -1;
		footerIhc = -1;
	}

	@Override
	public ViewGroup.MarginLayoutParams generateLayoutParams(AttributeSet attrs) {
		return new ViewGroup.MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected ViewGroup.MarginLayoutParams generateDefaultLayoutParams() {
		return new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected ViewGroup.MarginLayoutParams generateLayoutParams(
			ViewGroup.LayoutParams p) {
		return new ViewGroup.MarginLayoutParams(p);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof ViewGroup.MarginLayoutParams;
	}
}
