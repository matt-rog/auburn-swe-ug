-- datapath_aubie.vhd
-- entity reg_file (lab 2)
use work.dlx_types.all;
use work.bv_arithmetic.all;
entity reg_file is
	port (data_in: in dlx_word; readnotwrite,clock : in bit;
	data_out: out dlx_word; reg_number: in register_index );
end entity reg_file;
-- entity alu (lab 3)
-- arch of reg_file
architecture behaviour of reg_file is
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
end architecture behaviour; 
-- end arch of reg_file


use work.dlx_types.all;
use work.bv_arithmetic.all;
entity alu is
	generic(prop_delay : Time := 5 ns);
	port(operand1, operand2: in dlx_word; operation: in alu_operation_code;
	result: out dlx_word; error: out error_code);
end entity alu;
-- alu_operation_code values
-- 0000 unsigned add
-- 0001 signed add
-- 0010 2's compl add
-- 0011 2's compl sub
-- 0100 2's compl mul
-- 0101 2's compl divide
-- 0110 logical and
-- 0111 bitwise and
-- 1000 logical or
-- 1001 bitwise or
-- 1010 logical not (op1)
-- 1011 bitwise not (op1)
-- 1100-1111 output all zeros
-- error code values
-- 0000 = no error
-- 0001 = overflow (too big positive)
-- 0010 = underflow (too small neagative)
-- 0011 = divide by zero
-- arch of alu
architecture behaviour of alu is
begin
	aluProcess : process(operand1, operand2, operation) is
	-- init error states 
	variable ovf : boolean := false;
	variable dbz : boolean := false;
	-- init result storage
	variable temp_result : dlx_word;
	begin
        case(operation) is
		-- unsigned addition
		when "0000" =>
			bv_addu(operand1, operand2, temp_result, ovf);
			if ovf then
				error <= "0001" after prop_delay;
			else
				result <= temp_result after prop_delay;
			end if;

		-- unsigned subtraction
		when "0001" =>
			bv_subu(operand1, operand2, temp_result, ovf);
			if ovf then
				error <= "0001" after prop_delay;
			else
				result <= temp_result after prop_delay;
			end if;

		-- 2's comp. add
		when "0010" =>
			bv_add(operand1, operand2, temp_result, ovf);
			if ovf then
				error <= "0001" after prop_delay;
			else
				result <= temp_result after prop_delay;
			end if;
		
		-- 2's comp. sub
		when "0011" =>
			bv_sub(operand1, operand2, temp_result, ovf);
			if ovf then
				error <= "0001" after prop_delay;
			else
				result <= temp_result after prop_delay;
			end if;
		
		-- 2's comp. mult
		when "0100" =>
			bv_mult(operand1, operand2, temp_result, ovf);
			if ovf then
				error <= "0001" after prop_delay;
			else
				result <= temp_result after prop_delay;
			end if;
		
		-- 2's comp. div
		when "0101" =>
			bv_div(operand1, operand2, temp_result, dbz, ovf);
			if ovf or dbz then
				if ovf then
					error <= "0001" after prop_delay;
				else
					error <= "0010" after prop_delay;
				end if;
			else
				result <= temp_result after prop_delay;
			end if;

		-- bitwise and
		when "0111" =>
			result <= operand1 AND operand2 after prop_delay;

		-- bitwise or
		when "1001" =>
			result <= operand1 OR operand2 after prop_delay;

		-- logical not (op1)
		when "1010" =>
			if operand1 = x"00000000" then
				result <= x"00000001" after prop_delay;
			else
				result <= x"00000000" after prop_delay;
			end if;
		
		-- bitwise not (op1)
		when "1011" =>
			result <= NOT operand1 after prop_delay;

		-- output op1
		when "1100" =>
			result <= operand1 after prop_delay;
		
		-- output op2
		when "1101" =>
			result <= operand2 after prop_delay;

		-- 0's
		when "1110" =>
			result <= x"00000000" after prop_delay;		
		
		-- 1's
		when "1111" =>
			result <= x"11111111" after prop_delay;
			
		when others => result <= x"00000000" after prop_delay; -- default
	end case;
	end process aluProcess; 
end architecture behaviour; 
-- end arch of alu


-- entity dlx_register (lab 3)
use work.dlx_types.all;
entity dlx_register is
	generic(prop_delay : Time := 5 ns);
	port(in_val: in dlx_word; clock: in bit; out_val: out dlx_word);
end entity dlx_register;
-- end entity dlx_register
-- arch of dlx_register
architecture behaviour of dlx_register is
begin
	dlx_register_process : process(in_val, clock) is
	begin
		-- output = input when clock is high, output is frozen otherwise
		if clock = '1' then
			out_val <= in_val after prop_delay;
		end if;

	end process dlx_register_process; 
end architecture behaviour;
-- end arch of dlx_register


-- entity pcplusone
use work.dlx_types.all;
use work.bv_arithmetic.all;
entity pcplusone is
	generic(prop_delay: Time := 5 ns);
	port (input: in dlx_word; clock: in bit; output: out dlx_word);
end entity pcplusone;
-- end entity pcplusone
-- arch of pcplusone
architecture behaviour of pcplusone is
begin
	pcplusone_process : process(input, clock) is
	variable one : dlx_word;
	variable result : dlx_word;
	variable ovf : boolean := false;
	begin
		if clock = '1' then
			one := integer_to_bv(1, 32);
			bv_addu(input, one, result, ovf); -- increment by 1

			if ovf then
				result := integer_to_bv(0, 32); -- zero if overflow
				output <= result after prop_delay;
			end if;

			output <= result after prop_delay;
		end if;
	end process pcplusone_process; 
end architecture behaviour; 
-- end arch of pcplusone


-- entity mux
use work.dlx_types.all;
entity mux is
	generic(prop_delay : Time := 5 ns);
	port (input_1,input_0 : in dlx_word; which: in bit; output: out dlx_word);
end entity mux;
-- end entity mux
-- arch of mux
architecture behaviour of mux is
begin
	mux_process : process(input_0, input_1, which) is
	begin

		case(which) is
			when '0' =>
				output <= input_0 after prop_delay;
			when '1' =>
				output <= input_1 after prop_delay;
		end case;

	end process mux_process; 
end architecture behaviour; 
-- end arch of mux


-- entity threeway_mux
use work.dlx_types.all;
entity threeway_mux is
	generic(prop_delay : Time := 5 ns);
	port (input_2,input_1,input_0 : in dlx_word; which: in threeway_muxcode;
		output: out dlx_word);
end entity threeway_mux;
-- end entity threeway mux
-- arch of threeway mux
architecture behaviour of threeway_mux is
begin
	threeway_mux_process : process(input_0, input_1, input_2, which) is
	begin

		case(which) is
			when "00" =>
				output <= input_0 after prop_delay;
			when "01" =>
				output <= input_1 after prop_delay;
			when "11" =>
				output <= input_2 after prop_delay;
			when others =>
				output <= input_0 after prop_delay;
		end case;

	end process threeway_mux_process; 
end architecture behaviour; 
-- end arch of threeway mux


-- entity regfile
use work.dlx_types.all;
entity reg_file is
	port(data_in : in dlx_word; readnotwrite, clock: in bit; data_out: out
		dlx_word; reg_number : in register_index);
end entity reg_file;
-- end entity regfile
-- arch of regfile
architecture behaviour of reg_file is
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
end architecture behaviour; 
-- end arch of regfile


-- entity memory
use work.dlx_types.all;
use work.bv_arithmetic.all;
entity memory is
	port (
		address : in dlx_word;
		readnotwrite: in bit;
		data_out : out dlx_word;
		data_in: in dlx_word;
		clock: in bit);
end memory;
architecture behavior of memory is
	begin -- behavior
		mem_behav: process(address,clock) is
-- note that there is storage only for the first 1k of the memory, to speed
-- up the simulation
			type memtype is array (0 to 1024) of dlx_word;
			variable data_memory : memtype;
		begin
-- fill this in by hand to put some values in there
-- some instructions
			data_memory(0) := X"30200000"; --LD R4, 0x100
			data_memory(1) := X"00000100"; -- address 0x100 for previous instruction
			data_memory(2) := "00000000000110000100010000000000"; -- ADDU R3,R1,R2
-- some data
-- note that this code runs every time an input signal to memory changes,
-- so for testing, write to some other locations besides these
			data_memory(256) := "01010101000000001111111100000000";
			data_memory(257) := "10101010000000001111111100000000";
			data_memory(258) := "00000000000000000000000000000001";
			if clock = '1' then
				if readnotwrite = '1' then
-- do a read
					data_out <= data_memory(bv_to_natural(address)) after 5 ns;
				else
-- do a write
					data_memory(bv_to_natural(address)) := data_in;
				end if;
			end if;
		end process mem_behav;
end behavior;
-- end entity memory
