package com.minxing365.vote.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.minxing365.vote.dao.VotingDetailsMapper;
import com.minxing365.vote.pojo.Data;
import com.minxing365.vote.pojo.Restrict;
import com.minxing365.vote.pojo.ResultVo;
import com.minxing365.vote.pojo.VoteDetailsVo;
import com.minxing365.vote.service.VotingDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VotingDetailsServiceImpl implements VotingDetailsService {
    @Autowired
    VotingDetailsMapper votingDetailsMapper;

    @Override
    public String selectVotingDetailsByName(String name) {
          String nameStr="%"+name+"%";
        List<VoteDetailsVo> list = votingDetailsMapper.selectVotingDetailsByName(nameStr);
        List<ResultVo> resultVoList=new ArrayList<>();
          if (list.size()>0){
              for (int i=0;i<list.size();i++){
                       ResultVo resultVo=new ResultVo();
                         resultVo.setId(list.get(i).getId());
                         resultVo.setAppId(list.get(i).getAppId());
                         resultVo.setUserId(list.get(i).getUserId());
                         resultVo.setName(list.get(i).getName());
                      JSONObject jsonObject=JSONObject.parseObject(list.get(i).getBody());
                       resultVo.setLabel(jsonObject.getString("label"));
                       resultVo.setTip(jsonObject.getString("tip"));
                       resultVoList.add(resultVo);
              }
          }

//        JSONObject jsonObject;
//        VoteDetailsVo voteDetailsVo = new VoteDetailsVo();
//        try {
//            jsonObject = JSONObject.parseObject(result);
//            JSONArray array = jsonObject.getJSONArray("data");
//            List<Data> dataList = new ArrayList<>();
//            if (null != array || "".equals(array)) {
//
//                for (int i = 0; i < array.size(); i++) {
//                    Data data = new Data();
//                    JSONObject jo = array.getJSONObject(i);
//                    data.setLabel(jo.getString("label"));
//                    data.setValue(jo.getInteger("value"));
//                    dataList.add(data);
//                }
//            }
////            voteDetailsVo.setCondition(jsonObject.getBoolean("condition"));
////            voteDetailsVo.setType(jsonObject.getString("type"));
//            // voteDetailsVo.setName(jsonObject.getString("name"));
//            voteDetailsVo.setLabel(jsonObject.getString("label"));
//            voteDetailsVo.setTip(jsonObject.getString("tip"));
////            voteDetailsVo.setBind(jsonObject.getString("bind"));
////            voteDetailsVo.setComponentId(jsonObject.getString("componentId"));
////            voteDetailsVo.setValueClassName(jsonObject.getString("valueClassName"));
////            voteDetailsVo.setValid(jsonObject.getBoolean("valid"));
////            Restrict restrict = new Restrict();
////            JSONObject json_restrict = jsonObject.getJSONObject("restrict");
////            restrict.setLayout(json_restrict.getString("layout"));
////            restrict.setRequired(json_restrict.getBoolean("required"));
////            voteDetailsVo.setRestrict(restrict);
////            voteDetailsVo.setValue(jsonObject.getString("value"));
//            voteDetailsVo.setId(jsonObject.getInteger("id"));
//            voteDetailsVo.setData(dataList);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        JSONObject object = new JSONObject();
        object.put("list", resultVoList);
        object.put("message", "查询成功");
        return object.toJSONString();
    }
}
