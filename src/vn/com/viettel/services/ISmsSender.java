package vn.com.viettel.services;

import ws.bulkSms.impl.Result;

public interface ISmsSender {

    Result send(String username, String password, String cpCode, String sender, String receiver, String content, String contentType);
}
