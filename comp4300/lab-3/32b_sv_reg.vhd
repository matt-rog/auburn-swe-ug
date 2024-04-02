use work.dlx_types.all;
use work.bv_arithmetic.all;

-- 32-bit single-value register
entity dlx_register is
	generic(prop_delay : Time := 10 ns);
	port(in_val: in dlx_word; 
		clock: in bit; 
		out_val: out dlx_word);
end entity dlx_register;


architecture behaviour1 of dlx_register is
begin
	dlx_register_process : process(in_val, clock) is
	begin
		-- output = input when clock is high, output is frozen otherwise
		if clock = '1' then
			out_val <= in_val after prop_delay;
		end if;

	end process dlx_register_process; 
end architecture behaviour1; 