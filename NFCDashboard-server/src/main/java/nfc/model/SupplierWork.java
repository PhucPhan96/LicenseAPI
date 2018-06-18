package nfc.model;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_supplier_work")
public class SupplierWork {
    
    private int suppl_id;
    private boolean is_active;
    private String wd_start_hm;
    private String wd_end_hm;
    private String sat_start_hm;
    private String sat_end_hm;
    private String sun_start_hm;
    private String sun_end_hm;
    private boolean is_online;
    private BigDecimal rank5;
    private int order_count;
    private int favorite_count;
    private boolean call_order;
    private boolean direct_pay;
    private boolean visit_pay;
    private BigDecimal min_order_amt;
    private int owner_suppl_id;
    private int manage_suppl_id;
    private String suppl_role;
    private String suppl_rank;
    private int board_id;
    private String latitude;
    private String longitude;
    private BigDecimal rank_money;
    private Date rank_start_date;
    private Date rank_due_date;
    private String suppl_delivery_info;
    private String delivery_id;
    private String distance_in_km;
    private String work_day;
    private String closed_days;
    private String closed_day_type;
    
    @javax.persistence.Transient
    public String getDistance_in_km() {
        return distance_in_km;
    }

    public void setDistance_in_km(String distance_in_km) {
        this.distance_in_km = distance_in_km;
    }
    @Id
    @Column(name="suppl_id")
    public int getSuppl_id() {
            return suppl_id;
    }
    public void setSuppl_id(int suppl_id) {
            this.suppl_id = suppl_id;
    }
    @Column(name="suppl_role")
    public String getSuppl_role() {
            return suppl_role;
    }
    public void setSuppl_role(String suppl_role) {
            this.suppl_role = suppl_role;
    }
    @Column(name="is_active")
    public boolean getIs_active() {
            return is_active;
    }
    public void setIs_active(boolean is_active) {
            this.is_active = is_active;
    }
    @Column(name="wd_start_hm")
    public String getWd_start_hm() {
            return wd_start_hm;
    }
    public void setWd_start_hm(String wd_start_hm) {
            this.wd_start_hm = wd_start_hm;
    }
    @Column(name="wd_end_hm")
    public String getWd_end_hm() {
            return wd_end_hm;
    }
    public void setWd_end_hm(String wd_end_hm) {
            this.wd_end_hm = wd_end_hm;
    }
    @Column(name="sat_start_hm")
    public String getSat_start_hm() {
            return sat_start_hm;
    }
    public void setSat_start_hm(String sat_start_hm) {
            this.sat_start_hm = sat_start_hm;
    }
    @Column(name="sat_end_hm")
    public String getSat_end_hm() {
            return sat_end_hm;
    }
    public void setSat_end_hm(String sat_end_hm) {
            this.sat_end_hm = sat_end_hm;
    }
    @Column(name="sun_start_hm")
    public String getSun_start_hm() {
            return sun_start_hm;
    }
    public void setSun_start_hm(String sun_start_hm) {
            this.sun_start_hm = sun_start_hm;
    }
    @Column(name="sun_end_hm")
    public String getSun_end_hm() {
            return sun_end_hm;
    }
    public void setSun_end_hm(String sun_end_hm) {
            this.sun_end_hm = sun_end_hm;
    }
    @Column(name="is_online")
    public boolean getIs_online() {
            return is_online;
    }
    public void setIs_online(boolean is_online) {
            this.is_online = is_online;
    }
    @Column(name="rank5")
    public BigDecimal getRank5() {
            return rank5;
    }
    public void setRank5(BigDecimal rank5) {
            this.rank5 = rank5;
    }
    @Column(name="order_count")
    public int getOrder_count() {
            return order_count;
    }
    public void setOrder_count(int order_count) {
            this.order_count = order_count;
    }
    @Column(name="favorite_count")
    public int getFavorite_count() {
            return favorite_count;
    }
    public void setFavorite_count(int favorite_count) {
            this.favorite_count = favorite_count;
    }
    @Column(name="call_order")
    public boolean getCall_order() {
            return call_order;
    }
    public void setCall_order(boolean call_order) {
            this.call_order = call_order;
    }
    @Column(name="direct_pay")
    public boolean getDirect_pay() {
            return direct_pay;
    }
    public void setDirect_pay(boolean direct_pay) {
            this.direct_pay = direct_pay;
    }
    @Column(name="visit_pay")
    public boolean getVisit_pay() {
            return visit_pay;
    }
    public void setVisit_pay(boolean visit_pay) {
            this.visit_pay = visit_pay;
    }
    @Column(name="min_order_amt")
    public BigDecimal getMin_order_amt() {
            return min_order_amt;
    }
    public void setMin_order_amt(BigDecimal min_order_amt) {
            this.min_order_amt = min_order_amt;
    }
    @Column(name="owner_suppl_id")
    public int getOwner_suppl_id() {
            return owner_suppl_id;
    }
    public void setOwner_suppl_id(int owner_suppl_id) {
            this.owner_suppl_id = owner_suppl_id;
    }
    @Column(name="manage_suppl_id")
    public int getManage_suppl_id() {
            return manage_suppl_id;
    }
    public void setManage_suppl_id(int manage_suppl_id) {
            this.manage_suppl_id = manage_suppl_id;
    }	
    @Column(name="suppl_rank")
    public String getSuppl_rank() {
            return suppl_rank;
    }
    public void setSuppl_rank(String suppl_rank) {
            this.suppl_rank = suppl_rank;
    }
    @Column(name="board_id")
    public int getBoard_id() {
            return board_id;
    }
    public void setBoard_id(int board_id) {
            this.board_id = board_id;
    }
    
    @Column(name="latitude")
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    @Column(name="longitude")
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Column(name="rank_money")
    public BigDecimal getRank_money() {
        return rank_money;
    }

    public void setRank_money(BigDecimal rank_money) {
        this.rank_money = rank_money;
    }
    
    @Column(name="rank_start_date")
    public Date getRank_start_date() {
        return rank_start_date;
    }

    public void setRank_start_date(Date rank_start_date) {
        this.rank_start_date = rank_start_date;
    }
    
    @Column(name="rank_due_date")
    public Date getRank_due_date() {
        return rank_due_date;
    }

    public void setRank_due_date(Date rank_due_date) {
        this.rank_due_date = rank_due_date;
    }

    @Column(name="suppl_delivery_info")
    public String getSuppl_delivery_info() {
        return suppl_delivery_info;
    }

    public void setSuppl_delivery_info(String suppl_delivery_info) {
        this.suppl_delivery_info = suppl_delivery_info;
    }

    @Column(name="delivery_id")
    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    @Column(name="work_day")
    public String getWork_day() {
        return work_day;
    }

    public void setWork_day(String work_day) {
        this.work_day = work_day;
    }

    @Column(name="closed_days")
    public String getClosed_days() {
        return closed_days;
    }

    public void setClosed_days(String closed_days) {
        this.closed_days = closed_days;
    }

    @Column(name="closed_day_type")
    public String getClosed_day_type() {
        return closed_day_type;
    }

    public void setClosed_day_type(String closed_day_type) {
        this.closed_day_type = closed_day_type;
    }
    
    
        
    
        
}
