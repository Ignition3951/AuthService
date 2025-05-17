package com.utk.authservice.util;

import com.utk.authservice.dto.UserInfoDto;

public class ValidationUtil {

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\+?[0-9]{10,15}$";
        return phoneNumber.matches(phoneRegex);
    }

    public static boolean isValidPassword(String password) {
        // At least 8 characters, at least 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordRegex);
    }

    public static boolean isValidUserDetails(UserInfoDto userInfoDto){
        return isValidEmail(userInfoDto.getEmail()) &&
               isValidPhoneNumber(userInfoDto.getPhoneNumber().toString()) &&
               isValidPassword(userInfoDto.getPassword());
    }
}
