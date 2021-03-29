package com.kmadrugstore.utils;

public class Constants {
    public static final String EMPLOYEE_ROLE_STRING = "employee";
    public static final String GUEST_ROLE_STRING = "guest";
    public static final String USER_ROLE_STRING = "user";

    public static final int EMPLOYEE_ROLE_ID = 1;
    public static final int GUEST_ROLE_ID = 2;
    public static final int USER_ROLE_ID = 3;

    public static final String PENDING_STATUS_STRING = "очікує виконання";
    public static final String COMPLETED_STATUS_STRING = "виконано";
    public static final String PAID_STATUS_STRING = "оплачено";
    public static final String CANCELLED_STATUS_STRING = "скасовано";

    public static final int PENDING_STATUS_ID = 1;
    public static final int COMPLETED_STATUS_ID = 2;
    public static final int PAID_STATUS_ID = 3;
    public static final int CANCELLED_STATUS_ID = 4;

    public static final int ORDER_VALIDITY_IN_DAYS = 3;

    public static final int PASSWORD_RESET_TOKEN_VALIDITY_IN_MINUTES = 60;

    public static final String PENDING_STATUS_EMAIL = "Шановний(-а) %s %s !" +
            " Дякуємо, що скористались послугами KMADrugStore! Ваше замовлення вже " +
            "виконується, очікуйте на підтвердження найближчим часом. Гарного дня!";;

    public static final String COMPLETED_STATUS_EMAIL = "Вітаємо! Ваше замовлення #%s вже готове і " +
            "чекатиме на вас впродовж 3 днів! Сума до оплати: %s грн. " +
            "Не забудьте взяти з собою рецепти від лікаря, якщо це необхідно! ";

    public static final String PAID_STATUS_EMAIL = "Дякуємо за довіру нашій онлайн аптеці! Вам нараховано %s бонусів. " +
            "Вдалого дня! ";

    public static final String CANCELLED_STATUS_EMAIL = "Шановний(-а), %s %s ! На жаль, Ваше замовлення #%s скасоване! " +
            "Нагадуємо, що замовлення можна забрати лише протягом 3 днів, інакше воно скасовується! Вдалих майбутніх покупок!";

    public static final String RESET_PASSWORD_EMAIL =
            "Перейдіть за посиланням, щоб відновити пароль: %s";
    public static final String CHANGE_PASSWORD_URL = "/#/change-password?token=";

    public static final String ORDER_EMAIL_SUBJECT = "Замовлення з KMADrugStore";
    public static final String RESET_PASSWORD_EMAIL_SUBJECT = "Відновлення паролю KMADrugStore";

    public static final String GOOD_NOT_FOUND_MESSAGE = "Good not found";
    public static final String GOOD_NOT_IN_STOCK_MESSAGE = "Good is not in stock";
    public static final String STATUS_NOT_FOUND_MESSAGE = "Status not found";
    public static final String ORDER_NOT_FOUND_MESSAGE = "Order not found";
    public static final String USER_NOT_FOUND_MESSAGE = "User not found";
    public static final String EMAIL_IS_USED_MESSAGE = "Email is already used";
    public static final String NOT_ENOUGH_BONUSES_MESSAGE = "Not enough bonuses";
    public static final String PASSWORD_RESET_TOKEN_INVALID_EXCEPTION = "Password reset token is invalid";

    // User can receive 1 bonus for every 100 spent in one purchase.
    public static final int AMOUNT_FOR_BONUS = 100;
}
