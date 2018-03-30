import java.nio.ByteBuffer;
import java.util.*;
import java.text.*;

public class Record {
    public String register_name;
    public String bn_name;
    public String bn_status;
    public String bn_reg_dt;
    public String bn_cancel_dt;
    public String bn_renew_dt;
    public String bn_state_num;
    public String bn_state_of_reg;
    public long bn_abn;
    public short offset_register_name;
    public short offset_bn_name;
    public short offset_bn_status;
    public short offset_bn_reg_dt;
    public short offset_bn_cancel_dt;
    public short offset_bn_renew_dt;
    public short offset_bn_state_num;
    public short offset_bn_state_of_reg ;
    public short offset_bn_abn;
    public short offset_end;

    public Record(String line) {
        try {
            String[] str_array = new String[] {"","","","","","","","","0"};
            String[] fields_array = line.split("\t");
            System.arraycopy(fields_array, 0, str_array, 0, fields_array.length);
            this.register_name = str_array[0];
            this.bn_name = str_array[1];
            this.bn_status = str_array[2];
            this.bn_reg_dt = str_array[3];
            this.bn_cancel_dt = str_array[4];
            this.bn_renew_dt = str_array[5];
            this.bn_state_num = str_array[6];
            this.bn_state_of_reg = str_array[7];
            this.bn_abn = Long.parseLong(str_array[8]);
            this.offset_register_name = 0;
            this.offset_bn_name = (short) str_array[0].length();
            this.offset_bn_status =  (short) (this.offset_bn_name + str_array[1].length());
            this.offset_bn_reg_dt = (short) (this.offset_bn_status + str_array[2].length());
            this.offset_bn_cancel_dt = (short) (this.offset_bn_reg_dt + str_array[3].length());
            this.offset_bn_renew_dt = (short) (this.offset_bn_cancel_dt + str_array[4].length());
            this.offset_bn_state_num = (short) (this.offset_bn_renew_dt + str_array[5].length());
            this.offset_bn_state_of_reg = (short) (this.offset_bn_state_num + str_array[6].length());
            this.offset_bn_abn = (short) (this.offset_bn_state_of_reg + str_array[7].length());
            this.offset_end = (short) (this.offset_bn_abn + (Long.SIZE/Byte.SIZE));

        } catch (Exception e) {
            System.out.println(line);
            e.printStackTrace();
        }
    }

    public Record(byte[] bytes) {
        this.offset_register_name = 0;
        this.offset_register_name = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 0, 2)).getShort();
        this.offset_bn_name = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 2, 4)).getShort();
        this.offset_bn_status = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 4, 6)).getShort();
        this.offset_bn_reg_dt = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 6, 8)).getShort();
        this.offset_bn_cancel_dt = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 8, 10)).getShort();
        this.offset_bn_renew_dt = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 10, 12)).getShort();
        this.offset_bn_state_num = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 12, 14)).getShort();
        this.offset_bn_state_of_reg  = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 14, 16)).getShort();
        this.offset_bn_abn = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 16, 18)).getShort();
        this.offset_end = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 18, 20)).getShort();

        byte[] body = Arrays.copyOfRange(bytes, 20, bytes.length);
        try {
            this.register_name = new String(Arrays.copyOfRange(body, this.offset_register_name, this.offset_bn_name),
                    "US-ASCII");
            this.bn_name = new String(Arrays.copyOfRange(body, this.offset_bn_name, this.offset_bn_status),
                    "US-ASCII");
            this.bn_status = new String(Arrays.copyOfRange(body, this.offset_bn_status, this.offset_bn_reg_dt),
                    "US-ASCII");
            this.bn_reg_dt = new String(Arrays.copyOfRange(body, this.offset_bn_reg_dt, this.offset_bn_cancel_dt),
                    "US-ASCII");
            this.bn_cancel_dt = new String(Arrays.copyOfRange(body, this.offset_bn_cancel_dt, this.offset_bn_renew_dt),
                    "US-ASCII");
            this.bn_renew_dt = new String(Arrays.copyOfRange(body, this.offset_bn_renew_dt, this.offset_bn_state_num),
                    "US-ASCII");
            this.bn_state_num = new String(Arrays.copyOfRange(body, this.offset_bn_state_num, this.offset_bn_state_of_reg ),
                    "US-ASCII");
            this.bn_state_of_reg = new String(Arrays.copyOfRange(body, this.offset_bn_state_of_reg , this.offset_bn_abn),
                    "US-ASCII");
            this.bn_abn = ByteBuffer.allocate(Long.BYTES)
                    .put(Arrays.copyOfRange(body, this.offset_bn_abn, this.offset_end))
                    .flip().getLong();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    
    public byte[] serializer() {
        byte[] header = new byte[20];
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.offset_register_name).array(),
                0, header, 0, 2);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.offset_bn_name).array(),
                0, header, 2, 2);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.offset_bn_status).array(),
                0, header, 4, 2);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.offset_bn_reg_dt).array(),
                0, header, 6, 2);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.offset_bn_cancel_dt).array(),
                0, header, 8, 2);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.offset_bn_renew_dt).array(),
                0, header, 10, 2);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.offset_bn_state_num).array(),
                0, header, 12, 2);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.offset_bn_state_of_reg ).array(),
                0, header, 14, 2);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.offset_bn_abn).array(),
                0, header, 16, 2);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.offset_end).array(),
                0, header, 18, 2);
        byte[] body = new byte[this.offset_end];
        byte[] bn_abn = ByteBuffer.allocate(Long.SIZE/Byte.SIZE).putLong(this.bn_abn).array();
        // this.get_properties();
        System.arraycopy(this.register_name.getBytes(), 0, body, 0, this.register_name.length());
        System.arraycopy(this.bn_name.getBytes(), 0, body, this.offset_bn_name, this.bn_name.length());
        System.arraycopy(this.bn_status.getBytes(), 0, body, this.offset_bn_status, this.bn_status.length());
        System.arraycopy(this.bn_reg_dt.getBytes(), 0, body, this.offset_bn_reg_dt, this.bn_reg_dt.length());
        System.arraycopy(this.bn_cancel_dt.getBytes(), 0, body, this.offset_bn_cancel_dt, this.bn_cancel_dt.length());
        System.arraycopy(this.bn_renew_dt.getBytes(), 0, body, this.offset_bn_renew_dt, this.bn_renew_dt.length());
        System.arraycopy(this.bn_state_num.getBytes(), 0, body, this.offset_bn_state_num, this.bn_state_num.length());
        System.arraycopy(this.bn_state_of_reg.getBytes(), 0, body, this.offset_bn_state_of_reg, this.bn_state_of_reg.length());
        System.arraycopy(bn_abn, 0, body, this.offset_bn_abn, Long.SIZE/Byte.SIZE);
        byte[] result = new byte[20 + this.offset_end];
        System.arraycopy(header, 0, result, 0, 20);
        System.arraycopy(body, 0, result, 20, this.offset_end);
        return result;
    }

    private void get_properties() {
        System.out.println("register_name: " + this.register_name);
        System.out.println("bn_name: " + this.bn_name);
        System.out.println("bn_status: " + this.bn_status);
        System.out.println("bn_reg_dt: " + this.bn_reg_dt);
        System.out.println("bn_cancel_dt: " + this.bn_cancel_dt);
        System.out.println("bn_renew_dt: " + this.bn_renew_dt);
        System.out.println("bn_state_num: " + this.bn_state_num);
        System.out.println("bn_state_of_reg: " + this.bn_state_of_reg);
        System.out.println("bn_abn: " + this.bn_abn);
        System.out.println("offset_register_name: " + this.offset_register_name);
        System.out.println("offset_bn_name: " + this.offset_bn_name);
        System.out.println("offset_bn_status: " + this.offset_bn_status);
        System.out.println("offset_bn_reg_dt: " + this.offset_bn_reg_dt);
        System.out.println("offset_bn_cancel_dt: " + this.offset_bn_cancel_dt);
        System.out.println("offset_bn_renew_dt: " + this.offset_bn_renew_dt);
        System.out.println("offset_bn_state_num: " + this.offset_bn_state_num);
        System.out.println("offset_bn_state_of_reg : " + this.offset_bn_state_of_reg );
        System.out.println("offset_bn_abn: " + this.offset_bn_abn);
        System.out.println("offset_end: " + this.offset_end);
    }

    public short lenth() {
        return (short) (20 + this.offset_end);
    }

}
