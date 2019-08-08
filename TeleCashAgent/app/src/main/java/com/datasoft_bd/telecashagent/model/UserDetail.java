package com.datasoft_bd.telecashagent.model;

import com.datasoft.mfs.commons.model.UserAccount;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Masum on 7/29/2019
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class UserDetail extends UserAccount {
    private String name;
    private String email;
    private String gender;
    private Long userAccountId;

    @Override
    public String toString() {
        return name;
    }
}
