use work.dlx_types.all;
use work.bv_arithmetic.all;

-- PC Incr.
entity pcplusone is
	generic(prop_delay: Time := 5 ns);
	port (input: in dlx_word; 
		clock: in bit; 
		output: out dlx_word);
end entity pcplusone;

architecture behaviour1 of pcplusone is
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
end architecture behaviour1; 