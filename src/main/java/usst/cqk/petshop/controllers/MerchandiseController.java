package usst.cqk.petshop.controllers;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import usst.cqk.petshop.entities.*;
import usst.cqk.petshop.entities.repositories.AccountRepository;
import usst.cqk.petshop.entities.repositories.IndentRepository;
import usst.cqk.petshop.entities.repositories.MerchandiseRepository;
import usst.cqk.petshop.entities.repositories.TrolleyRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class MerchandiseController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MerchandiseRepository merchandiseRepository;
    @Autowired
    private TrolleyRepository trolleyRepository;
    @Autowired
    private IndentRepository indentRepository;

    @GetMapping(path = "/getMerchandiseList/{phoneNumber}")
    public @ResponseBody
    String getMerchandiseList(@PathVariable("phoneNumber") String phoneNumber) {
        List<Merchandise> foundMerchandiseList;
        if (phoneNumber == null || phoneNumber.equals("-1")) {
            foundMerchandiseList = (List<Merchandise>) merchandiseRepository.findAll();
        } else {
            foundMerchandiseList=merchandiseRepository.findMerchandisesByAccountId(accountRepository.findAccountByPhoneNumber(phoneNumber).getAccountId());
        }
        List<TransMerchandise> foundTransMerchandiseList = changMerchandiseListToTransMerchandiseList(foundMerchandiseList);
        return JSON.toJSONString(foundTransMerchandiseList);
    }

    private List<TransMerchandise> changMerchandiseListToTransMerchandiseList(List<Merchandise> merchandiseList) {
        List<TransMerchandise> transMerchandiseList = new ArrayList<TransMerchandise>();
        for (Merchandise merchandise : merchandiseList) {
            TransMerchandise transMerchandise = new TransMerchandise();
            transMerchandise.setMerchandiseId(merchandise.getMerchandiseId());
            transMerchandise.setAccountId(merchandise.getAccountId());
            transMerchandise.setMerchandiseName(merchandise.getMerchandiseName());
            transMerchandise.setPrice(merchandise.getPrice());
            transMerchandise.setTag(merchandise.getTag());
            transMerchandise.setSellerNickName(accountRepository.findAccountByAccountId(merchandise.getAccountId()).getNickName());
            transMerchandise.setPointMerchandise(merchandise.getPointMerchandise());
            transMerchandiseList.add(transMerchandise);
        }
        return transMerchandiseList;
    }

    @GetMapping(path = "/getPointMerchandiseList")
    public @ResponseBody
    String getPointMerchandiseList() {
        List<Merchandise> foundMerchandiseList = merchandiseRepository.findMerchandisesByPointMerchandise(true);
        List<TransMerchandise> foundTransMerchandiseList = changMerchandiseListToTransMerchandiseList(foundMerchandiseList);
        return JSON.toJSONString(foundTransMerchandiseList);
    }

    @PostMapping(path = "/exchangePointMerchandise")
    public @ResponseBody
    String exchangePointMerchandise(@RequestBody String requestData) {
        TransTrolley requestTransTrolley = JSON.parseObject(requestData, TransTrolley.class);
        Merchandise foundMerchandise = merchandiseRepository.findMerchandiseByMerchandiseId(requestTransTrolley.getMerchandiseId());
        Account foundAccount = accountRepository.findAccountByPhoneNumber(requestTransTrolley.getPhoneNumber());
        Integer neededPoints = Math.round(foundMerchandise.getPrice()) * requestTransTrolley.getNumber();
        if (foundAccount.getPoint() < neededPoints) {
            return JSON.toJSONString("no enough points");
        } else {
            foundAccount.setPoint(foundAccount.getPoint() - neededPoints);
            accountRepository.save(foundAccount);
            Indent newIndent = new Indent();
            newIndent.setStatus(1);
            newIndent.setAccountId(foundAccount.getAccountId());
            newIndent.setMerchandiseId(foundMerchandise.getMerchandiseId());
            newIndent.setSellerId(foundMerchandise.getAccountId());
            newIndent.setSellerNickName(accountRepository.findAccountByAccountId(foundMerchandise.getAccountId()).getNickName());
            newIndent.setMerchandiseName(foundMerchandise.getMerchandiseName());
            newIndent.setTag(foundMerchandise.getTag());
            newIndent.setPrice(foundMerchandise.getPrice());
            newIndent.setNumber(requestTransTrolley.getNumber());
            newIndent.setDate(new Date());
            indentRepository.save(newIndent);
            return JSON.toJSONString("exchange successfully");
        }
    }

    @PostMapping(path = "/deletePointMerchandise")
    public @ResponseBody
    String deletePointMerchandise(@RequestBody String requestData) {
        Long requestMerchandiseId = JSON.parseObject(requestData, Long.class);
        Merchandise foundMerchandise = merchandiseRepository.findMerchandiseByMerchandiseId(requestMerchandiseId);
        foundMerchandise.setPointMerchandise(false);
        merchandiseRepository.save(foundMerchandise);
        return JSON.toJSONString("delete successfully");
    }

    @PostMapping(path = "/addPointMerchandise")
    public @ResponseBody
    String addPointMerchandise(@RequestBody String requestData) {
        Long requestMerchandiseId = JSON.parseObject(requestData, Long.class);
        Merchandise foundMerchandise = merchandiseRepository.findMerchandiseByMerchandiseId(requestMerchandiseId);
        foundMerchandise.setPointMerchandise(true);
        merchandiseRepository.save(foundMerchandise);
        return JSON.toJSONString("add successfully");
    }

    @PostMapping(path = "/seller/addNewMerchandise")
    public @ResponseBody
    String addNewMerchandise(@RequestBody String requestData) {
        TransMerchandise requestTransMerchandise = JSON.parseObject(requestData, TransMerchandise.class);
        Merchandise newMerchandise = new Merchandise();
        newMerchandise.setMerchandiseName(requestTransMerchandise.getMerchandiseName());
        newMerchandise.setAccountId(accountRepository.findAccountByPhoneNumber(requestTransMerchandise.getPhoneNumber()).getAccountId());
        newMerchandise.setTag(requestTransMerchandise.getTag());
        newMerchandise.setPrice(requestTransMerchandise.getPrice());
        merchandiseRepository.save(newMerchandise);
        return JSON.toJSONString("add successfully");
    }

    @Transactional
    @PostMapping(path = "/seller/deleteMerchandise")
    public @ResponseBody
    String deleteMerchandise(@RequestBody String requestData) {
        Long merchandiseId = JSON.parseObject(requestData, Long.class);
        merchandiseRepository.deleteMerchandiseByMerchandiseId(merchandiseId);
        trolleyRepository.deleteTrolleysByMerchandiseId(merchandiseId);
        List<Indent> foundIndentList=indentRepository.findIndentsByMerchandiseIdAndStatus(merchandiseId,0);
        for (Indent foundIndent : foundIndentList) {
            foundIndent.setStatus(2);
            indentRepository.save(foundIndent);
        }
        return JSON.toJSONString("delete successfully");
    }

    @PostMapping(path = "/modifyMerchandise")
    public @ResponseBody
    String modifyMerchandise(@RequestBody String requestData) {
        Merchandise requestMerchandise = JSON.parseObject(requestData, Merchandise.class);
        if (requestMerchandise.getMerchandiseId() == null) {
            return JSON.toJSONString("modify failed");
        } else {
            Merchandise foundMerchandise = merchandiseRepository.findMerchandiseByMerchandiseId(requestMerchandise.getMerchandiseId());
            if (requestMerchandise.getMerchandiseName() != null) {
                foundMerchandise.setMerchandiseName(requestMerchandise.getMerchandiseName());
            }
            if (requestMerchandise.getTag() != null) {
                foundMerchandise.setTag(requestMerchandise.getTag());
            }
            if (requestMerchandise.getPrice() != null) {
                foundMerchandise.setPrice(requestMerchandise.getPrice());
            }
            merchandiseRepository.save(foundMerchandise);
            return JSON.toJSONString("modify successfully");
        }
    }

    @PostMapping(path = "/addMerchandiseToTrolley")
    public @ResponseBody
    String addMerchandiseToTrolley(@RequestBody String requestData) {
        TransTrolley requestTransTrolley = JSON.parseObject(requestData, TransTrolley.class);
        Trolley requestTrolley = new Trolley();
        requestTrolley.setNumber(requestTransTrolley.getNumber());
        requestTrolley.setMerchandiseId(requestTransTrolley.getMerchandiseId());
        requestTrolley.setAccountId(accountRepository.findAccountByPhoneNumber(requestTransTrolley.getPhoneNumber()).getAccountId());
        Trolley foundTrolley = trolleyRepository.findTrolleyByAccountIdAndMerchandiseId(requestTrolley.getAccountId(), requestTrolley.getMerchandiseId());
        if (foundTrolley == null) {
            trolleyRepository.save(requestTrolley);
        } else {
            foundTrolley.setNumber(foundTrolley.getNumber()+requestTrolley.getNumber());
            trolleyRepository.save(foundTrolley);
        }
        return JSON.toJSONString("add merchandise to trolley successfully");
    }

    @GetMapping(path = "/getMyTrolleyList/{phoneNumber}")
    public @ResponseBody
    String getMyTrolleyList(@PathVariable("phoneNumber") String phoneNumber) {
        List<Trolley> myTrolleyList = trolleyRepository.findTrolleysByAccountId(accountRepository.findAccountByPhoneNumber(phoneNumber).getAccountId());
        List<TransTrolley> myTransTrolleyList = new ArrayList<TransTrolley>();
        for (Trolley myTrolley : myTrolleyList) {
            TransTrolley newTransTrolley = new TransTrolley();
            newTransTrolley.setTrolleyId(myTrolley.getTrolleyId());
            newTransTrolley.setMerchandiseId(myTrolley.getMerchandiseId());
            newTransTrolley.setMerchandiseName(merchandiseRepository.findMerchandiseByMerchandiseId(myTrolley.getMerchandiseId()).getMerchandiseName());
            newTransTrolley.setSellerNickName(accountRepository.findAccountByPhoneNumber(phoneNumber).getNickName());
            newTransTrolley.setNumber(myTrolley.getNumber());
            newTransTrolley.setTag(merchandiseRepository.findMerchandiseByMerchandiseId(myTrolley.getMerchandiseId()).getTag());
            newTransTrolley.setPrice(merchandiseRepository.findMerchandiseByMerchandiseId(myTrolley.getMerchandiseId()).getPrice());
            myTransTrolleyList.add(newTransTrolley);
        }
        return JSON.toJSONString(myTransTrolleyList);
    }

    @Transactional
    @PostMapping(path = "/deleteTrolley")
    public @ResponseBody
    String deleteTrolley(@RequestBody String requestData) {
        Long requestTrolleyId = JSON.parseObject(requestData, Long.class);
        trolleyRepository.deleteTrolleyByTrolleyId(requestTrolleyId);
        return JSON.toJSONString("delete trolley successfully");
    }

}
