package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import nfc.model.PKModel.ProgramRolePK;

@Entity
@Table(name="fg_program_role")
@IdClass(ProgramRolePK.class)
public class ProgramRole {
	
    @Id
    private String program_id;
    @Id
    private String app_id;
    @Id
    private int role_id;
        
    @Column(name="program_id")
    public String getProgram_id() {
            return program_id;
    }
    
    public void setProgram_id(String program_id) {
            this.program_id = program_id;
    }
    
    @Column(name="app_id")
    public String getApp_id() {
            return app_id;
    }
    
    public void setApp_id(String app_id) {
            this.app_id = app_id;
    }
    
    @Column(name="role_id")
    public int getRole_id() {
            return role_id;
    }
     
    public void setRole_id(int role_id) {
            this.role_id = role_id;
    }
	
}
