package com.kmadrugstore.service;

public interface IEmailService {

    void sendMessageToEmail(final String email, final String subj,
                            final String message);
}