package com.kmadrugstore.utils;

import com.kmadrugstore.entity.Role;

public class RoleFactory {

    private static final Role employeeRole = new Role(Constants.EMPLOYEE_ROLE_ID,
            Constants.EMPLOYEE_ROLE_STRING);
    private static final Role guestRole = new Role(Constants.GUEST_ROLE_ID,
            Constants.GUEST_ROLE_STRING);
    private static final Role userRole = new Role(Constants.USER_ROLE_ID,
            Constants.USER_ROLE_STRING);

    public static Role employee() {
        return employeeRole;
    }

    public static Role guest() {
        return guestRole;
    }

    public static Role user() {
        return userRole;
    }
}
