package com.wxxr.mobile.el;

public class Date {
    int year, month, date;

    public Date(int year, int month, int date) {
        this.year = year;
        this.month = month;
        this.date = date;
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDate() { return date; }

    public String toString() {
        return "" + month + "/" + date + "/" + year;
    }

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(int date) {
		this.date = date;
	}
}
