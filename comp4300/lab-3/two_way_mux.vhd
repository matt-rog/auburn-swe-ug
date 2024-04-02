use work.dlx_types.all;
use work.bv_arithmetic.all;

-- 2-Way Mux
entity mux is
	generic(prop_delay : Time := 5 ns);
	port (input_1,input_0: in dlx_word; 
		which: in bit; 
		output: out dlx_word);
end entity mux;

architecture behaviour1 of mux is
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
end architecture behaviour1; 