package u8c.vo.arrival;



public class ArrivalData {
	
   private String arrivalRegiCode;//业务到账登记号--交易流水号
   private String payerCode;//支付方编号 u8c客商编号
   private String payerName;//支付方名称 u8c客商名称
   private String arrivalDate;//到账时间 trans_date交易日期
   private String currency;//币种 c_ccynbr 转换币种代码
   private Double arrivalAmount;//到账金额 trsamt 原币金额
   private String arrivalPurpose;//款项用途  trans_abstr 摘要
   private String operatorCode;//经办人    操作员编码
   private String comCode;//经办人机构编码 公司编码
   private String comName;
   public void setArrivalRegiCode(String arrivalRegiCode) {
        this.arrivalRegiCode = arrivalRegiCode;
    }
    public String getArrivalRegiCode() {
        return arrivalRegiCode;
    }

   public void setPayerCode(String payerCode) {
        this.payerCode = payerCode;
    }
    public String getPayerCode() {
        return payerCode;
    }

   public void setPayerName(String payerName) {
        this.payerName = payerName;
    }
    public String getPayerName() {
        return payerName;
    }

   public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
    public String getArrivalDate() {
        return arrivalDate;
    }

   public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getCurrency() {
        return currency;
    }

   public void setArrivalAmount(Double arrivalAmount) {
        this.arrivalAmount = arrivalAmount;
    }
    public Double getArrivalAmount() {
        return arrivalAmount;
    }

   public void setArrivalPurpose(String arrivalPurpose) {
        this.arrivalPurpose = arrivalPurpose;
    }
    public String getArrivalPurpose() {
        return arrivalPurpose;
    }

   public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
    public String getOperatorCode() {
        return operatorCode;
    }

   public void setComCode(String comCode) {
        this.comCode = comCode;
    }
    public String getComCode() {
        return comCode;
    }

   public void setComName(String comName) {
        this.comName = comName;
    }
    public String getComName() {
        return comName;
    }

}