package com.fib.uias.service;

import com.fib.uias.entity.UserEntity;

public interface IUserService {
	public UserEntity getUser(String userCode);

	public int addUser(UserEntity userEntity);
	
	void retryTransferAccounts(int fromAccountId, int toAccountId, float money) throws Exception;
}
