package u8c.vo.arrival;



public class ArrivalData {
	
   private String arrivalRegiCode;//ҵ���˵ǼǺ�--������ˮ��
   private String payerCode;//֧������� u8c���̱��
   private String payerName;//֧�������� u8c��������
   private String arrivalDate;//����ʱ�� trans_date��������
   private String currency;//���� c_ccynbr ת�����ִ���
   private Double arrivalAmount;//���˽�� trsamt ԭ�ҽ��
   private String arrivalPurpose;//������;  trans_abstr ժҪ
   private String operatorCode;//������    ����Ա����
   private String comCode;//�����˻������� ��˾����
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