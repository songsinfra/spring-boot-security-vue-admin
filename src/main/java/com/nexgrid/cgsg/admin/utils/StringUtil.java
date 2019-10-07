package com.nexgrid.cgsg.admin.utils;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtil {

	public static boolean empty(Object obj) {
		if(obj == null) return true;
		
		if(obj instanceof String) {
			return ((String)obj).equals("");
		} else if(obj instanceof List) {
			return ((List)obj).isEmpty();
		} else if(obj instanceof Map) {
			return ((Map)obj).isEmpty();
		} else if(obj instanceof Object[]) {
			return Array.getLength(obj) == 0;
		} else {
			return obj == null;
		}
	}

	public static String nvl(String str) {
		return nvl(str, "");
	}

	public static String nvl(String str, String sDefault) {
		if (str == null) return sDefault;
		else return str;
	}

	public static String nvl(Long n) {
		return nvl(n, "");
	}

	public static String nvl(Long n, String sDefault) {
		if (n == null) {
			return sDefault;
		}
		return n.toString();
	}

	public static String nvl(Integer n) {
		return nvl(n, "");
	}

	public static String nvl(Integer n, String sDefault) {
		if (n == null) {
			return sDefault;
		}
		return n.toString();
	}

	public static String nvl(Float f) {
		return nvl(f, "");
	}

	public static String nvl(Float f, String sDefault) {
		if (f == null) {
			return sDefault;
		}
		return f.toString();
	}

	public static String nvl(Double f) {
		return nvl(f, "");
	}

	public static String nvl(Double f, String sDefault) {
		if (f == null) {
			return sDefault;
		}
		return f.toString();
	}
	
	
	// CTN을 12자리로 만들어 준다.

	public static String getNcas444(String ctn) {
		int leng = ctn.length();
		switch (leng) {
		case 10:
			ctn = ctn.substring(0, 3) + "00" + ctn.substring(3, 10);
			break;
		case 11:
			ctn = ctn.substring(0, 3) + "0" + ctn.substring(3, 11);
			break;
		default:
			break;
		}
		return ctn;

	}
	
   // CTN을 11자리로 만들어 준다.
   public static String getCtn344(String ctn) {
      StringBuffer ctn344 = new StringBuffer();

      int len = ctn.length();
      if (len == 10) {
         ctn344.append(ctn.substring(0, 3));
         ctn344.append("0");
         ctn344.append(ctn.substring(3, len));
      } else if (len == 12) {
         ctn344.append(ctn.substring(0, 3));
         ctn344.append(ctn.substring(4, len));
      } else if (len == 11) {
         ctn344.append(ctn);
      }

      return ctn344.toString();
   }
   
   
   
   
   /**
    * 통계 값 포맷
    * @param val
    * @return
    */
   public static String getMsgPrice(int val) {
      DecimalFormat df = new DecimalFormat("###,##0");
      return df.format(val).toString();
   }
   
   public static boolean isStringInteger(String s) {
	   
	   try {
		   
		   Integer.parseInt(s);
		   return true;
		   
	   } catch (NumberFormatException e) {
		   
		   return false;
		   
	   }
   }
   
   
   public static String getfinalPrice(String val) {
	   
	   if(val != null && !StringUtil.isStringInteger(val) && Integer.parseInt(val) > 100) {
			
			return StringUtil.getMsgPrice(Integer.parseInt(val));
			
		} else {
			
			return val;
		}
   }

	public static String convertNewListToDd(String newLine) {
		String[] textList = newLine.split("\n");

		StringBuilder sb = new StringBuilder();

		for (String text : textList) {
			sb.append(String.format("<dd>%s</dd>", text));
		}

		return sb.toString();
	}

	public static String convertDdToNewLine(String dd) {
		Matcher matcher = Pattern.compile("<dd>(.+?)</dd>").matcher(dd);

		List<String> textList = new ArrayList<>();
		while (matcher.find()) textList.add(matcher.group(1));

		return textList.stream().collect(Collectors.joining("\n"));
	}

}
