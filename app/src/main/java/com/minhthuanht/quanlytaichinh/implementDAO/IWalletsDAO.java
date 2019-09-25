package com.minhthuanht.quanlytaichinh.implementDAO;

import com.minhthuanht.quanlytaichinh.model.Wallet;

import java.util.List;

public interface IWalletsDAO {

    boolean insertWallet(Wallet wallet);
    boolean updateWallet(Wallet wallet) ;
    boolean deleteWallet(long walletId) ;
    Wallet getWalletById(int id);
    boolean hasWallet(String userId);
    List<Wallet> getAllWalletByUser(String userId);
    long getIDMax();
}
