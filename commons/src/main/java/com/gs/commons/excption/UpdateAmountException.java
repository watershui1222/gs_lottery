package com.gs.commons.excption;

/**
 * 业务异常
 * 
 * @author ruoyi
 */
public final class UpdateAmountException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */

    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public UpdateAmountException()
    {
    }

    public UpdateAmountException(String message)
    {
        this.message = message;
    }

    public UpdateAmountException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }

    public UpdateAmountException setMessage(String message)
    {
        this.message = message;
        return this;
    }
}