package com.wxxr.mobile.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wxxr.mobile.core.util.ObjectUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class RootLayout extends ViewGroup {

	private Context context;
	private LayoutInflater layoutInflater;
	private Map<String, View> overMaps = new HashMap<String, View>();//id,override view
	private Map<String, View> ParamsMaps = new HashMap<String, View>();//id,overed view
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
//		List<String> valueList = mapValue2List(overMaps);
		int childCount = getChildCount();
		// 布局非遮盖view并保存params
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			int identity = System.identityHashCode(child);
			if (overMaps.containsValue(child)
					|| child.getVisibility() == View.GONE) {
				continue;
			}
			if (identity == headerIhc) {// 布局headerView
				ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child
						.getLayoutParams();
				for (String viewid : keyList) {
					int id = 0;
					try {
						id = Integer.valueOf(viewid);
					} catch (NumberFormatException e) {
					}
					View v = child.findViewById(id);
					if (v != null) {
						ParamsMaps.put(viewid, v);
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
				for (String viewid : keyList) {
					int id = 0;
					try {
						id = Integer.valueOf(viewid);
					} catch (NumberFormatException e) {
					}
					View v = child.findViewById(id);
					if (v != null) {
						ParamsMaps.put(viewid, v);
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
				for (String viewid : keyList) {
					int id = 0;
					try {
						id = Integer.valueOf(viewid);
					} catch (NumberFormatException e) {
					}
					View v = child.findViewById(id);
					if (v != null) {
						ParamsMaps.put(viewid, v);
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
			for (String viewid : keyList) {
				int id = 0;
				try {
					id = Integer.valueOf(viewid);
				} catch (NumberFormatException e) {
				}
				View v = content.findViewById(id);
				if (v != null) {
					ParamsMaps.put(viewid, v);
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
			if (overMaps.containsValue(child)) {
//				String id = overMaps.get(identity);
				String id = "";
				Object obj = child.getTag();
				if(obj != null) {
					id = (String)obj;
				}
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

	public void addProgressOverrideView(View v, Map<String, Object> params) {
		View add = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				RUtils.getInstance().getResourceId(RUtils.CATEGORY_NAME_LAYOUT,
						"progress_layout"), null);
		add.setOnClickListener(null);
		ViewGroup group = (ViewGroup) v.getParent();
		if (group == null)
			return;
		int index = -1;
		int count = group.getChildCount();
		for (int j = 0; j < count; j++) {
			View vj = group.getChildAt(j);
			if (ObjectUtils.isEquals(v, vj)) {
				index = j;
				break;
			}
		}
		if (index < 0)
			return;
		if (true) {
			ViewGroup.LayoutParams p = v.getLayoutParams();
			FrameLayout frame = new FrameLayout(context);
			frame.setTag(this);
			group.removeView(v);
			frame.addView(v);
			frame.addView(add);
			group.addView(frame, index, p);
		}
	}
	
	public void addProgressOverrideView(int id, Map<String, Object> params) {
		View add = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				RUtils.getInstance().getResourceId(RUtils.CATEGORY_NAME_LAYOUT,
						"progress_layout"), null);
		if (overMaps.containsKey(id + "")) {
			removeOverrideView(id);
//			View v = overMaps.get(id + "");
//			removeView(v);
		}
		if (overMaps.containsValue(add))
			throw new IllegalStateException(
					"The specified child already has a parent. You must call removeView() on the child's parent first.");
		add.setTag(id + "");
		overMaps.put(id + "", add);
		add.setOnClickListener(null);
//		add.requestLayout();
//		add.postInvalidate();
//		requestLayout();
//		postInvalidate();
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			View v = child.findViewById(id);
			if(v != null) {
				ViewGroup group = (ViewGroup)v.getParent();
				if(group == null)
					return;
				int index = -1;
				int count = group.getChildCount();
				for(int j = 0; j < count;j++) {
					View vj = group.getChildAt(j);
					if(ObjectUtils.isEquals(v, vj)) {
						index = j;
						break;
					}
				}
				if(index < 0)
					return;
				if(true) {
					ViewGroup.LayoutParams p = v.getLayoutParams();
					FrameLayout frame = new FrameLayout(context);
					frame.setTag(this);
//					LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(p.width, p.height);
//					frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//					pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
					group.removeView(v);
					frame.addView(v);
					frame.addView(add);
					group.addView(frame, index, p);
				} else {
				if(group instanceof LinearLayout) {
					LinearLayout.LayoutParams p = (LinearLayout.LayoutParams)v.getLayoutParams();
					FrameLayout frame = new FrameLayout(context);
					frame.setTag(this);
//					LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(p.width, p.height);
//					frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//					pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
					group.removeView(v);
					frame.addView(v);
					frame.addView(add);
					group.addView(frame, index, p);
				} else if(group instanceof RelativeLayout) {
					RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams)v.getLayoutParams();
					FrameLayout frame = new FrameLayout(context);
					frame.setTag(this);
//					RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams(p.width, p.height);
//					frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//					pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
					group.removeView(v);
					frame.addView(v);
					frame.addView(add);
					group.addView(frame, index, p);
				} else if(group instanceof FrameLayout) {
					FrameLayout.LayoutParams p = (FrameLayout.LayoutParams)v.getLayoutParams();
					FrameLayout frame = new FrameLayout(context);
					frame.setTag(this);
//					FrameLayout.LayoutParams pa = new FrameLayout.LayoutParams(p.width, p.height);
//					frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//					pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
					group.removeView(v);
					frame.addView(v);
					frame.addView(add);
					group.addView(frame, index, p);
				}
				}
			}
		}
//		addView(add);
	}
	
	public void addFailedOverrideView(int id, Map<String, Object> params,
			final IReloadCallBack cb) {
		View add = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				RUtils.getInstance().getResourceId(RUtils.CATEGORY_NAME_LAYOUT,
						"progress_failed_layout"), null);
		if (overMaps.containsKey(id + "")) {
			removeOverrideView(id);
//			View v = overMaps.get(id + "");
//			removeView(v);
		}
		if (overMaps.containsValue(add))
			throw new IllegalStateException(
					"The specified child already has a parent. You must call removeView() on the child's parent first.");
		add.setTag(id + "");
		overMaps.put(id + "", add);
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				cb.onReload(null);
			}
		});
//		add.requestLayout();
//		add.postInvalidate();
//		requestLayout();
//		postInvalidate();
//		View v = add.findViewById(RUtils.getInstance().getResourceId(RUtils.CATEGORY_NAME_ID,
//						"failed"));
//		if(v != null)
//			v.requestLayout();
		
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			View v = child.findViewById(id);
			if(v != null) {
				ViewGroup group = (ViewGroup)v.getParent();
				if(group == null)
					return;
				int index = -1;
				int count = group.getChildCount();
				for(int j = 0; j < count;j++) {
					View vj = group.getChildAt(j);
					if(ObjectUtils.isEquals(v, vj)) {
						index = j;
						break;
					}
				}
				if(index < 0)
					return;
				if(true) {
					ViewGroup.LayoutParams p = v.getLayoutParams();
					FrameLayout frame = new FrameLayout(context);
					frame.setTag(this);
//					LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(p.width, p.height);
//					frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//					pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
					group.removeView(v);
					frame.addView(v);
					frame.addView(add);
					group.addView(frame, index, p);
				} else {
				if(group instanceof LinearLayout) {
					LinearLayout.LayoutParams p = (LinearLayout.LayoutParams)v.getLayoutParams();
					FrameLayout frame = new FrameLayout(context);
					frame.setTag(this);
//					LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(p.width, p.height);
//					frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//					pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
					group.removeView(v);
					frame.addView(v);
					frame.addView(add);
					group.addView(frame, index, p);
				} else if(group instanceof RelativeLayout) {
					RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams)v.getLayoutParams();
					FrameLayout frame = new FrameLayout(context);
					frame.setTag(this);
//					RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams(p.width, p.height);
//					frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//					pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
					group.removeView(v);
					frame.addView(v);
					frame.addView(add);
					group.addView(frame, index, p);
				} else if(group instanceof FrameLayout) {
					FrameLayout.LayoutParams p = (FrameLayout.LayoutParams)v.getLayoutParams();
					FrameLayout frame = new FrameLayout(context);
					frame.setTag(this);
//					FrameLayout.LayoutParams pa = new FrameLayout.LayoutParams(p.width, p.height);
//					frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//					pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
					group.removeView(v);
					frame.addView(v);
					frame.addView(add);
					group.addView(frame, index, p);
				}
				}
			}
		}
//		addView(add);
	}
	
	public void removeOverrideView(int id) {
		if(overMaps.containsKey(id+"")) {
			View view = overMaps.get(id + "");
			overMaps.remove(id+"");
			
			int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				View child = getChildAt(i);
				View v = child.findViewById(id);
				if(v != null) {
					ViewGroup frame = (ViewGroup)v.getParent();
					if(frame instanceof FrameLayout && ObjectUtils.isEquals(frame.getTag(), this)) {
						ViewGroup frameParent = (ViewGroup)frame.getParent();
						if(frameParent == null)
							return;
						int index = -1;
						int count = frameParent.getChildCount();
						for(int j = 0; j < count;j++) {
							View vj = frameParent.getChildAt(j);
							if(ObjectUtils.isEquals(frame, vj)) {
								index = j;
								break;
							}
						}
						if(index < 0)
							return;
						if(true) {
							ViewGroup.LayoutParams p = frame.getLayoutParams();
//							LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(p.width, p.height);
//								frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//							pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
							frame.removeView(v);
							frame.removeView(view);
							frameParent.removeView(frame);
							frameParent.addView(v, index, p);
						} else {
						if(frameParent instanceof LinearLayout) {
							LinearLayout.LayoutParams p = (LinearLayout.LayoutParams)frame.getLayoutParams();
//							LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(p.width, p.height);
//								frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//							pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
							frame.removeView(v);
							frame.removeView(view);
							frameParent.removeView(frame);
							frameParent.addView(v, index, p);
						} else if(frameParent instanceof RelativeLayout) {
							RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams)frame.getLayoutParams();
//							RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams(p.width, p.height);
//								frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//							pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
							frame.removeView(v);
							frame.removeView(view);
							frameParent.removeView(frame);
							frameParent.addView(v, index, p);
						} else if(frameParent instanceof FrameLayout) {
							FrameLayout.LayoutParams p = (FrameLayout.LayoutParams)frame.getLayoutParams();
//							FrameLayout.LayoutParams pa = new FrameLayout.LayoutParams(p.width, p.height);
//							frame.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
//							pa.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
							frame.removeView(v);
							frame.removeView(view);
							frameParent.removeView(frame);
							frameParent.addView(v, index, p);
						}
						}
					}
				}
			}
//			removeView(view);
		}
	}
	
//	public void addOverrideView(View view, int id) {
//		if (overMaps.containsValue(view))
//			throw new IllegalStateException(
//					"The specified child already has a parent. You must call removeView() on the child's parent first.");
//		view.setTag(id+"");
//		overMaps.put(id + "", view);
//		addView(view);
//	}

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
		if (overMaps.containsValue(view)) {
			Object obj = view.getTag();
			if(obj != null) {
				String id = (String)obj;
				overMaps.remove(id);
			}
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

	public View getProgressView() {
		return ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				RUtils.getInstance().getResourceId(RUtils.CATEGORY_NAME_LAYOUT,
						"progress_layout"), null);
	}
	
	public View getProgressFailedView() {
		return ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				RUtils.getInstance().getResourceId(RUtils.CATEGORY_NAME_LAYOUT,
						"progress_failed_layout"), null);
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
