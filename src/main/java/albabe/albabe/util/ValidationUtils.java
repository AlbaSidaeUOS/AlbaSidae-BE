package albabe.albabe.util;

import java.util.regex.Pattern;

/*
    Description : 생년월일 양식 검증하는 소스파일
 */

public class ValidationUtils {

    // 수정된 정규 표현식: YYMMDD 형식
    private static final String BIRTH_DATE_REGEX = "^(00|0[1-9]|[1-9][0-9])(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$";
    private static final Pattern BIRTH_DATE_PATTERN = Pattern.compile(BIRTH_DATE_REGEX);

    public static boolean isValidBirthDate(String birthDate) {
        if (birthDate == null || !BIRTH_DATE_PATTERN.matcher(birthDate).matches()) {
            return false; // 기본 형식 검증 실패
        }

        try {
            int year = Integer.parseInt(birthDate.substring(0, 2));
            int month = Integer.parseInt(birthDate.substring(2, 4));
            int day = Integer.parseInt(birthDate.substring(4, 6));

            // YY -> YYYY 변환 (기본적으로 1900~2099로 가정)
            year += (year < 50) ? 2000 : 1900;

            // 달별 최대 일수
            int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            // 윤년이면 2월의 일수를 29로 설정
            if (month == 2 && isLeapYear(year)) {
                daysInMonth[1] = 29;
            }

            return day <= daysInMonth[month - 1];
        } catch (Exception e) {
            return false; // 예외 발생 시 유효하지 않은 날짜로 간주
        }
    }

    // 윤년 확인
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
