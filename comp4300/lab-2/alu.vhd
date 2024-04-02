use work.dlx_types.all;
use work.bv_arithmetic.all;

entity alu is
	generic(prop_delay: Time := 15 ns);
	port(operand1, operand2: in dlx_word; operation: in
	alu_operation_code;
	result: out dlx_word; error: out error_code);
end entity alu;

architecture behaviour1 of alu is
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
end architecture behaviour1; 