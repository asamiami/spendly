package com.project.spendly2.util;

import org.springframework.beans.factory.annotation.Value;

public class FlutterwaveEndpoints {

    @Value("${flutterwave.url}")
    private String baseUrl;

    private static final String BASE_URL = "https://api.flutterwave.com/v3/";

    public static final String VIRTUAL_ACCOUNT_NUMBER = BASE_URL + "virtual-account-numbers";

    public static final String TRANSFER = BASE_URL + "transfers";

    public static final String GET_BANKS = BASE_URL + "banks/:country";

    public static final String VERIFY_BANK_ACCOUNT = BASE_URL + "accounts/resolve";

    public static final String CREATE_SUBACCOUNT = BASE_URL + "payout-subaccounts";

}

