use work.dlx_types.all;
use work.bv_arithmetic.all;

-- Register File
entity reg_file is
	generic(r_prop_delay : Time := 10 ns);
	port(data_in: in dlx_word; 
		clock,readnotwrite: in bit; 
		data_out: out dlx_word; 
		reg_number: in register_index);
end entity reg_file;

architecture behaviour1 of reg_file is
type reg_type is array (0 to 31) of dlx_word;
begin
	reg_file_process : process(data_in, clock, readnotwrite, reg_number) is
	variable registers : reg_type;
	variable reg_number_int : integer;
	begin

		if clock = '1' then -- 1 r or w (not both) per cycle

			reg_number_int := bv_to_integer(reg_number);

			if readnotwrite = '1' then -- read operation		
				data_out <= registers(reg_number_int) after r_prop_delay;
			else -- write operation
				registers(reg_number_int) := data_in;
			end if;

		end if;
			

	end process reg_file_process; 
end architecture behaviour1; 