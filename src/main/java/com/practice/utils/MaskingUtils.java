package com.practice.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MaskingUtils {

	public static String masking(String target, String masking_type) {
		if (masking_type.equals("NAME")) {
			return maskingForName(target);
		} else if (masking_type.equals("ID")) {
			return maskingForId(target);
		} else if (masking_type.equals("MOBILE")) {
			return maskingForCellphone(target);
		} else if (masking_type.equals("EMAIL")) {
			return maskingForEmail(target);
		}

		return target;
	}

	public static String maskingForName(String name) {
		if (StringUtils.isNotBlank(name)) {
			name = StringUtils.deleteWhitespace(name);

			// 내국인 Check
			if (name.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
				return maskingAfterIndex(name, 2, "*");
			} else {
				return maskingAfterIndex(name, 5, "*");
			}
		}

		return name;
	}

	public static String maskingForId(String id) {
		if (StringUtils.isNotBlank(id)) {
			id = StringUtils.deleteWhitespace(id);

			return maskingAfterIndex(id, 5, "*");
		}

		return id;
	}

	public static String maskingForCellphone(String cellphone) {
		if (StringUtils.isBlank(cellphone) || cellphone.length() < 12) {
			return cellphone;
		}

		return maskingToDelimiter(cellphone, "-", 1, "*");
	}

	/**
	 * 휴대폰 번호 마스킹(010****1234 / 011***1234)
	 *
	 * @param phoneNumber
	 * @return maskedPhoneNum
	 */
	public static String maskPhoneNumber(String phoneNumber) {

		String maskedPhoneNum = phoneNumber;
		// 공백제거
		maskedPhoneNum = maskedPhoneNum.replaceAll(" ", "");

		// '-'가 포함되어있으면 모두 삭제
		if (maskedPhoneNum.contains("-")) {
			maskedPhoneNum = maskedPhoneNum.replaceAll("[^0-9]", "");
		}

		// 11자리 또는 10자리가 되지 않으면 공백 ""
		// 11자리 휴대폰 번호 마스킹 처리
		if (maskedPhoneNum.length() == 11) {
			String num1 = maskedPhoneNum.substring(0, 3);
			String num3 = maskedPhoneNum.substring(7);

			maskedPhoneNum = num1 + "****" + num3;
			// 10자리 휴대폰 번호 마스킹 처리
		} else if (maskedPhoneNum.length() == 10) {
			String num1 = maskedPhoneNum.substring(0, 3);
			String num3 = maskedPhoneNum.substring(6);
			maskedPhoneNum = num1 + "***" + num3;
		} else {
			maskedPhoneNum = "연락처가 잘못 입력되어 있습니다.";
		}

		return maskedPhoneNum;
	}

	public static String maskingForMobile(String cellphone) {
		if (StringUtils.isBlank(cellphone) || cellphone.length() < 11) {
			return cellphone;
		}

//		return maskingToDelimiter(cellphone, "-", 1, "*");
		return maskString(cellphone, 3, 7, '*');
	}

	/**
	 * @param strText
	 * @param start
	 * @param end
	 * @param maskChar
	 * @return strText 문자열을 index 위치에서부터 end 까지 maskChar문자로 마스킹 처리 한다.
	 * ex) maskString("01055559999", 3, 7, '*') = 010****9999
	 */
	private static String maskString(String strText, int start, int end, char maskChar) {

		if (strText == null || strText.equals(""))
			return "";

		if (start < 0)
			start = 0;

		if (end > strText.length())
			end = strText.length();

		int maskLength = end - start;

		if (maskLength == 0)
			return strText;

		String strMaskString = StringUtils.repeat(maskChar, maskLength);

		return StringUtils.overlay(strText, strMaskString, start, end);
	}

	/**
	 * @param target
	 * @param index
	 * @param replacement
	 * @return target 문자열을 index 위치에서부터 replacement문자로 마스킹 처리 한다.
	 * ex) maskingAfterIndex("abcdefg", 5, "*") = abcd***
	 */
	public static String maskingAfterIndex(String target, int index, String replacement) {
		if (StringUtils.isBlank(target) || index < 1 || StringUtils.isBlank(replacement)) {
			return target;
		}
		String regex = "(?<=.{" + (index - 1) + "}).";
		return target.replaceAll(regex, replacement);
	}

	/**
	 * @param target
	 * @param delimiter
	 * @param delimiterPosition
	 * @param replacement
	 * @return target 을 replacement로 마스킹 처리 한다.
	 * delimiter 의 beginIndex 번째 부터 다음 delimiter 사이의 문자를 마스킹한다.
	 * maskingToDelimiter("1234-5678-9012-3456", "-", 2, "*") = 1234-5678-****-3456
	 * maskingToDelimiter("010-5658-2777", "-", 2, "*") = 010-****-2777
	 */
	public static String maskingToDelimiter(String target, String delimiter, int delimiterPosition, String replacement) {
		if (StringUtils.isBlank(target) || StringUtils.isBlank(delimiter) || delimiterPosition < 1 || StringUtils.isBlank(replacement)) {
			return target;
		}

		List<Integer> positions = new ArrayList<>();
		int idx = target.indexOf(delimiter);

		while (idx > -1) {
			positions.add(idx);
			idx = target.indexOf(delimiter, idx + 1);
		}
		if (positions.size() > delimiterPosition - 1) {
			int position = positions.get(delimiterPosition - 1);
			int fromIndex = target.indexOf(delimiter, position) + 1;
			int endIndex = target.indexOf(delimiter, fromIndex) - 1;
			if (target.length() >= fromIndex && target.length() >= endIndex) {
				int repeat = target.substring(fromIndex, endIndex).length() + 1;
				return target.substring(0, fromIndex) + StringUtils.repeat(replacement, repeat) + target.substring(target.indexOf(delimiter, fromIndex), target.length());
			}
		}

		return target;
	}

	public static String maskingForEmail(String email) {
		if (StringUtils.isBlank(email) || !isEmail(email)) {
			return email;
		} else {
			String[] emailArray = email.split("@");

			String preEmail = emailArray[0];
			String sufEmail = emailArray[1];

			int maxlength = preEmail.length();
			preEmail = preEmail.substring(0, maxlength - 1) + "*";

			return preEmail + "@" + sufEmail;
		}
	}

	public static String maskingToBeforeDelimiter(String target, String delimiter, int delimiterBeforeIndex, String replacemeent) {
		int delimiterPosition = target.indexOf(delimiter, delimiterBeforeIndex);
		int repeat = target.substring(delimiterBeforeIndex - 1, delimiterPosition).length();
		return target.substring(0, delimiterBeforeIndex) + StringUtils.repeat(replacemeent, repeat) + target.subSequence(delimiterPosition, target.length());
	}

	public static boolean isEmail(String email) {
		if (StringUtils.isBlank(StringUtils.trimToNull(email))) {
			return false;
		}

		return Pattern.matches("[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", StringUtils.trimToNull(email));
	}

}
