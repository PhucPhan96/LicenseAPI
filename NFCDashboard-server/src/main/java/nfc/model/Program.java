package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="fg_programs")
public class Program {
	
    private String program_id;
    private String app_id;
    private String prog_name;
    private String parent_prog_id;
    private int display_seq;
    private String use_yn;
    private String program_url;
    private String program_class;

    @Id
    @Column(name="program_id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
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
    @Column(name="prog_name")
    public String getProg_name() {
            return prog_name;
    }
    public void setProg_name(String prog_name) {
        this.prog_name = prog_name;
    }
    @Column(name="parent_prog_id")
    public String getParent_prog_id() {
        return parent_prog_id;
    }
    public void setParent_prog_id(String parent_prog_id) {
        this.parent_prog_id = parent_prog_id;
    }
    @Column(name="display_seq")
    public int getDisplay_seq() {
        return display_seq;
    }
    public void setDisplay_seq(int display_seq) {
        this.display_seq = display_seq;
    }
    @Column(name="use_yn")
    public String getUse_yn() {
        return use_yn;
    }
    public void setUse_yn(String use_yn) {
        this.use_yn = use_yn;
    }
    @Column(name="program_url")
    public String getProgram_url() {
        return program_url;
    }
    public void setProgram_url(String program_url) {
        this.program_url = program_url;
    }
    @Column(name="program_class")
    public String getProgram_class() {
        return program_class;
    }
    public void setProgram_class(String program_class) {
        this.program_class = program_class;
    }
	
}
