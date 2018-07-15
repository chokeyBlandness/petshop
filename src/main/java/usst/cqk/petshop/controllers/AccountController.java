package usst.cqk.petshop.controllers;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import usst.cqk.petshop.entities.Account;
import usst.cqk.petshop.entities.repositories.AccountRepository;
import usst.cqk.petshop.entities.repositories.PetRepository;


@RestController
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PetRepository petRepository;

    @PostMapping(path = "/account/login", value = "/account/login")
    public @ResponseBody
    String accountLogin(@RequestBody String requestData) {
        String loginMessage;
        Account loginAccount = JSON.parseObject(requestData, Account.class);
        Account foundAccount = accountRepository.findAccountByPhoneNumber(loginAccount.getPhoneNumber());
        if (foundAccount == null) {
            loginMessage = "no this account";
        } else if (!foundAccount.getPassword().equals(loginAccount.getPassword())) {
            loginMessage = "wrong password";
        } else {
            if (foundAccount.getAccountLevel() == 2) {
                if (foundAccount.getStatus() == 1) {
                    loginMessage = String.valueOf(foundAccount.getAccountLevel());
                } else {
                    loginMessage = "wait workers to check";
                }
            } else {
                loginMessage = String.valueOf(foundAccount.getAccountLevel());
            }
        }
        return JSON.toJSONString(loginMessage);
    }

    @PostMapping(path = "/account/register", value = "/account/register")
    public @ResponseBody
    String accountRegister(@RequestBody String requestData) {
        String registerMessage;
        Account registerAccount = JSON.parseObject(requestData, Account.class);
        Account foundAccount = accountRepository.findAccountByPhoneNumber(registerAccount.getPhoneNumber());
        if (foundAccount != null) {
            registerMessage = "exist account";
        } else {
            if (registerAccount.getAccountLevel() == 1) {
                accountRepository.save(registerAccount);
                registerMessage = "register successfully";
            } else {
                // TODO: 2018/7/13  remove next line to excel it
                registerAccount.setStatus(0);
                accountRepository.save(registerAccount);
                registerMessage = "wait workers to check";
            }
        }
        return JSON.toJSONString(registerMessage);
    }

    @GetMapping(path = "/account/getMyPoint/{phoneNumber}")
    public @ResponseBody
    String getMyPoint(@PathVariable("phoneNumber") String phoneNumber) {
        Account foundAccount = accountRepository.findAccountByPhoneNumber(phoneNumber);
        return JSON.toJSONString(foundAccount.getPoint());
    }

    @GetMapping(path = "/account/getMyPetList/{phoneNumber}")
    public @ResponseBody
    String getMyPetList(@PathVariable("phoneNumber") String phoneNumber) {
        Account foundAccount = accountRepository.findAccountByPhoneNumber(phoneNumber);
        return JSON.toJSONString(petRepository.findPetsByAccountId(foundAccount.getAccountId()));
    }

    @PostMapping(path = "/account/addNewAccountByWorker")
    public @ResponseBody
    String addNewAccountByWorker(@RequestBody String requestData) {
        Account requestAccount = JSON.parseObject(requestData, Account.class);
        Account foundAccount = accountRepository.findAccountByPhoneNumber(requestAccount.getPhoneNumber());
        if (foundAccount != null) {
            return JSON.toJSONString("exist account");
        } else {
            accountRepository.save(requestAccount);
            return JSON.toJSONString("add successfully");
        }
    }

    @GetMapping(path = "/account/getWaitingAccount")
    public @ResponseBody
    String getWaitingAccount() {
        return JSON.toJSONString(accountRepository.findAccountsByStatus(0));
    }

    @PostMapping(path = "/account/permitSeller", value = "/account/permitSeller")
    public @ResponseBody
    String permitSeller(@RequestBody String requestData) {
        Long permitAccountId = JSON.parseObject(requestData, Long.class);
        Account permitAccount = accountRepository.findAccountByAccountId(permitAccountId);
        if (permitAccount == null) {
            return JSON.toJSONString("no this account");
        } else if (permitAccount.getStatus() == 1) {
            return JSON.toJSONString("already permitted");
        }
        permitAccount.setStatus(1);
        accountRepository.save(permitAccount);
        return JSON.toJSONString("permit successfully");
    }
}
