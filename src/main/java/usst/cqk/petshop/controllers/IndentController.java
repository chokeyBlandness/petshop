package usst.cqk.petshop.controllers;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import usst.cqk.petshop.entities.*;
import usst.cqk.petshop.entities.repositories.*;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@RestController
public class IndentController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MerchandiseRepository merchandiseRepository;
    @Autowired
    private TrolleyRepository trolleyRepository;
    @Autowired
    private IndentRepository indentRepository;
    @Autowired
    private PetRepository petRepository;

    @Transactional
    @PostMapping(path = "/orderIndent")
    public @ResponseBody
    String orderIndent(@RequestBody String requestData) {
        String phoneNumber = JSON.parseObject(requestData, String.class);
        Long accountId = accountRepository.findAccountByPhoneNumber(phoneNumber).getAccountId();
        List<Trolley> myTrolleyList = trolleyRepository.findTrolleysByAccountId(accountId);
        trolleyRepository.deleteTrolleysByAccountId(accountId);
        for (Trolley myTrolley : myTrolleyList) {
            Indent newIndent = new Indent();
            Merchandise foundMerchandise = merchandiseRepository.findMerchandiseByMerchandiseId(myTrolley.getMerchandiseId());
            newIndent.setAccountId(accountId);
            newIndent.setMerchandiseId(myTrolley.getMerchandiseId());
            newIndent.setMerchandiseName(foundMerchandise.getMerchandiseName());
            newIndent.setSellerId(foundMerchandise.getAccountId());
            newIndent.setSellerNickName(accountRepository.findAccountByAccountId(foundMerchandise.getAccountId()).getNickName());
            newIndent.setTag(foundMerchandise.getTag());
            newIndent.setPrice(foundMerchandise.getPrice());
            newIndent.setNumber(myTrolley.getNumber());
            newIndent.setDate(new Date());
            indentRepository.save(newIndent);
        }
        return JSON.toJSONString("order successfully");
    }

    @PostMapping(path = "/cancelIndent")
    public @ResponseBody
    String cancelIndent(@RequestBody String requestData) {
        Long requestIndentId = JSON.parseObject(requestData, Long.class);
        Indent foundIndent = indentRepository.findIndentByIndentId(requestIndentId);
        if (foundIndent.getStatus() == 1) {//1 represents received
            return JSON.toJSONString("already received,cannot cancel");
        }
        foundIndent.setStatus(2);//2 represents canceled
        indentRepository.save(foundIndent);
        return JSON.toJSONString("cancel successfully");
    }

    @PostMapping(path = "/finishIndent")
    public @ResponseBody
    String finishIndent(@RequestBody String requestData) {
        Long requestIndentId = JSON.parseObject(requestData, Long.class);
        Indent foundIndent = indentRepository.findIndentByIndentId(requestIndentId);
        foundIndent.setStatus(3);//3 represents finished
        indentRepository.save(foundIndent);
        Account foundAccount = accountRepository.findAccountByAccountId(foundIndent.getAccountId());
        foundAccount.setPoint((int) (foundAccount.getPoint()+foundIndent.getNumber()*foundIndent.getPrice()/10));
        accountRepository.save(foundAccount);
        if (foundIndent.getTag() == 1) {
            Pet newPet = new Pet();
            newPet.setAccountId(foundIndent.getAccountId());
            newPet.setSellerNickName(foundIndent.getSellerNickName());
            newPet.setPetName(foundIndent.getMerchandiseName());
            petRepository.save(newPet);
        }
        return JSON.toJSONString("finished");
    }

    @GetMapping(path = "/getMyIndentList/{phoneNumber}")
    public @ResponseBody
    String getMyIndentList(@PathVariable("phoneNumber") String phoneNumber) {
        List<Indent> myIndentList = indentRepository.findIndentsByAccountId(
                accountRepository.findAccountByPhoneNumber(phoneNumber).getAccountId()
        );
        return JSON.toJSONString(myIndentList);
    }
    @GetMapping(path = "/getReceivingIndentList/{phoneNumber}")
    public @ResponseBody
    String getReceivingIndentList(@PathVariable("phoneNumber") String phoneNumber) {
        List<Indent> receivingIndentList = indentRepository.findIndentsBySellerIdAndStatus(
                accountRepository.findAccountByPhoneNumber(phoneNumber).getAccountId(),0
        );
        return JSON.toJSONString(receivingIndentList);
    }

    @PostMapping(path = "/receiveIndent")
    public @ResponseBody
    String receiveIndent(@RequestBody String requestData) {
        Long requestIndentId = JSON.parseObject(requestData, Long.class);
        Indent foundIndent = indentRepository.findIndentByIndentId(requestIndentId);
        foundIndent.setStatus(1);
        indentRepository.save(foundIndent);
        return JSON.toJSONString("receive successfully");
    }
}
