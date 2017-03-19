package com.itheima.common.utils;

public class StringUtils {

	private StringUtils() {
		super();
	}

	/**
	 * is empty string.
	 * 
	 * @param str source string.
	 * @return is empty.
	 */
	public static boolean isEmpty(String str)
	{
		if( str == null || str.length() == 0 )
			return true;
		return false;
	}

	/**
	 * is not empty string.
	 * 
	 * @param str source string.
	 * @return is not empty.
	 */
    public static boolean isNotEmpty(String str)
    {
        return str != null && str.length() > 0;
    }
}
