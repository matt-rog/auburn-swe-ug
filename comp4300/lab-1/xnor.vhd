entity xnorGate is 
	generic(prop_delay: Time := 10 ns);
	port(
		a_in,b_in,c_in: in bit;
          	result: out bit
	);
end entity xnorGate; 


architecture behaviour1 of xnorGate is
begin
	xnorProcess : process(a_in, b_in, c_in) is 
	
	begin
        	if a_in = '1' then
			if b_in = '1' then
				if c_in = '0' then
					result <= '1' after prop_delay;
				else
					result <= '0' after prop_delay;
				end if;
			else
				if c_in = '1' then
					result <= '1' after prop_delay;
				else
					result <= '0' after prop_delay;
				end if;
			end if;
		else
			if b_in = '1' then
				if c_in = '1' then
					result <= '1' after prop_delay;
				else
					result <= '0' after prop_delay;
				end if;
			else
				if c_in = '1' then
					result <= '0' after prop_delay;
				else
					result <= '1' after prop_delay;
				end if;
			end if;
                end if;
	end process xnorProcess; 
end architecture behaviour1; 