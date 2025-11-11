package com.tennistime.reservation.application.dto.payment;

import jakarta.validation.constraints.NotBlank;

/**
 * Representation of the callback payload sent by SEP after the customer leaves the payment page.
 */
public class PaymentCallbackRequest {

    @NotBlank
    private String state;

    @NotBlank
    private String status;

    private String refNum;

    @NotBlank
    private String resNum;
    private String rrn;
    private String traceNo;
    private Long amount;
    private String terminalId;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public String getResNum() {
        return resNum;
    }

    public void setResNum(String resNum) {
        this.resNum = resNum;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
