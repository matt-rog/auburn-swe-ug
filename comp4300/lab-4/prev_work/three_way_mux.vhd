use work.dlx_types.all;
use work.bv_arithmetic.all;

-- 3-Way Mux
entity threeway_mux is
	generic(prop_delay : Time := 5 ns);
	port (input_2,input_1,input_0 : in dlx_word; 
		which: in threeway_muxcode;
		output: out dlx_word);
end entity threeway_mux;

architecture behaviour1 of threeway_mux is
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
end architecture behaviour1; 