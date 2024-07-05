package com.adnan.server.controllers;

import com.adnan.server.dataAccess.SkillDataAccess;
import com.adnan.server.dataAccess.UserDataAccess;
import com.adnan.server.models.Skill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class SkillController {
    private static SkillDataAccess SDA = null;
    private static UserDataAccess UDA = null;
    public SkillController() throws SQLException {
        SDA = new SkillDataAccess();
        UDA = new UserDataAccess();
    }
    public String createSkill(String userId, String text) throws SQLException {
        if(!UDA.userExists(userId))
            return "USER NOT FOUND!!!";
        if(SDA.countSkills(userId) == 5)
            return "YOU CAN ONLY ADD 5 SKILLS!!!";
        int a = SDA.countSkills(userId);
        ArrayList<Skill> skills = SDA.getSkills(userId);
        Skill skill;
        int x = 1;
        if (a != 0) {
            for (int i = 0; i < a ; i++) {
                if (skills.get(i).getNthSkill() != x) {
                    a = x - 1;
                    break;
                }
                x++;
            }
            skill = new Skill(userId, ++a, text);
        }
        else {
            skill = new Skill(userId, ++a, text);
        }
        SDA.addSkill(skill);
        return "successful";
    }
    public String getSkills(String userId) throws SQLException, JsonProcessingException {
        ArrayList<Skill> skills = SDA.getSkills(userId);
        if (skills == null) return "NO SKILLS!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(skills);
    }
    public String getSkills() throws SQLException, JsonProcessingException {
        ArrayList<Skill> skills = SDA.getSkills();
        if (skills == null) return "NO SKILLS!";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(skills);
    }
    public String deleteSkill(String userId, int nthSkill) throws SQLException {
        if (!SDA.skillExists(userId))
            return "SKILL NOT FOUND!!!";
        SDA.deleteSkill(userId, nthSkill);
        return "successful";
    }
    public String updateSkill(String userId, int nthSkill, String text) throws SQLException {
        if (!SDA.skillExists(userId) || SDA.countSkills(userId) < nthSkill)
            return "SKILL NOT FOUND!!!";
        Skill skill = SDA.getSkills(userId).get(nthSkill);
        skill.setText(text);
        SDA.updateSkill(skill);
        return "successful!";
    }
}
