#
#  Parser Class
#
load "TinyToken.rb"
load "TinyLexer.rb"
class Parser < Lexer
	def initialize(filename)
    	super(filename)
        @errors = 0
    	consume()
  end
   	
	def consume()
      	@lookahead = nextToken()
      	while(@lookahead.type == Token::WS)
        	@lookahead = nextToken()
      	end
  end
  	
	def match(*dtypes)
      	if (! dtypes.include? @lookahead.type)
            if (dtypes.length() > 1)
                dtypes.map!(&:upcase)
            end
            puts "Expected #{dtypes.join(" or ")} found #{@lookahead.text}"
            @errors += 1
        end
        consume()
  end

  # PGM		 -->   STMTSEQ
	def program()
      while( @lookahead.type != Token::EOF)
        statementSeq()
      end
      puts "There were #{@errors} parse errors found."
  end

  # STMTSEQ    -->   STMT+
  def statementSeq()
    if (@lookahead.text != ";" and @lookahead.text != "=")
      enter_rule("STMTSEQ")
    end
    statement()
    if (@lookahead.text != ";" and @lookahead.text != "=")
      exit_rule("STMTSEQ")
    end
  end

  # STMT       -->   ASSIGN   |   "print"  EXP   | IFSTMT | LOOPSTMT
  def statement()
    enter_rule("STMT")

    # "print" EXP
		if (@lookahead.type == Token::PRINT)
			puts "Found PRINT Token: #{@lookahead.text}"
			match(Token::PRINT)
			exp()

    # IFSTMT
    elsif (@lookahead.type == Token::IFOP)
      if_statement()

    # LOOPSTMT
    elsif (@lookahead.type == Token::WHILEOP)
      loop_statement()

    # ASSIGN
    else
      assign()
		end
    exit_rule("STMT")
  end

  # IFSTMT	 -->   "if" COMPARISON "then" STMTSEQ "end"
  def if_statement()
    enter_rule("IFSTMT")
    if (@lookahead.type == Token::IFOP)
      found_token("IFOP")
    end
    match(Token::IFOP)
    comparison()
    if (@lookahead.type == Token::THENOP)
      found_token("THENOP")
    end
    match(Token::THENOP)
    statementSeq()
    if (@lookahead.type == Token::ENDOP)
      found_token("ENDOP")
    end
    match(Token::ENDOP)
    exit_rule("IFSTMT")
  end

  # LOOPSTMT	 -->   "while" COMPARISON "then" STMTSEQ "end"
  def loop_statement()
    enter_rule("LOOPSTMT")
    if (@lookahead.type == Token::WHILEOP)
      found_token("WHILEOP")
    end
    match(Token::WHILEOP)
    comparison()
    if (@lookahead.type == Token::THENOP)
      found_token("THENOP")
    end
    match(Token::THENOP)
    statementSeq()
    if (@lookahead.type == Token::ENDOP)
      found_token("ENDOP")
    end
    match(Token::ENDOP)
    exit_rule("LOOPSTMT")
  end

  # COMPARISON -->   FACTOR ( "<" | ">" | "&" ) FACTOR
  def comparison()
    enter_rule("COMPARISON")
    factor()

    # ( "<" )
    if(@lookahead.type == Token::LT)
      found_token("LT")
      match(Token::LT)

    # ( ">" )
    elsif(@lookahead.type == Token::GT)
      found_token("GT")
      match(Token::GT)

    # ( "&" )
    else
      found_token("ANDOP")
      match(Token::ANDOP)
    end
    factor()
    exit_rule("COMPARISON")
  end

  # ASSIGN     -->   ID  "="  EXP
  def assign()
      enter_rule("ASSGN")

      # ID "=" EXP
      if (@lookahead.type == Token::ID)
          found_token("ID")
      end
      match(Token::ID)
      if (@lookahead.type == Token::ASSGN)
          found_token("ASSGN")
      end
      match(Token::ASSGN)
      exp()
      exit_rule("ASSGN")
  end

  # EXP        -->   TERM   ETAIL
  def exp()
      enter_rule("EXP")

      # TERM ETAIL
      term()
      etail()
      exit_rule("EXP")
  end

  # TERM       -->   FACTOR  TTAIL
  def term()
        enter_rule("TERM")

        # FACTOR TTAIL
        factor()
        ttail()
        exit_rule("TERM")
  end

  # FACTOR     -->   "(" EXP ")" | INT | ID
  def factor()
      enter_rule("FACTOR")

      # "(" EXP "}"
      if (@lookahead.type == Token::LPAREN)
          found_token("LPAREN")
          match(Token::LPAREN)
          exp()
          if (@lookahead.type == Token::RPAREN)
              found_token("RPAREN")
          end
          match(Token::RPAREN)

      # INT
      elsif (@lookahead.type == Token::INT)
          found_token("INT")
          match(Token::INT)

      # ID
      elsif (@lookahead.type == Token::ID)
          found_token("ID")
          match(Token::ID)
      else
          match(Token::LPAREN, Token::INT, Token::ID)
      end
      exit_rule("FACTOR")
  end

  # TTAIL      -->   "*" FACTOR TTAIL  | "/" FACTOR TTAIL | EPSILON
  def ttail()
      enter_rule("TTAIL")

      # "*" FACTOR TTAIL
      if (@lookahead.type == Token::MULTOP)
          found_token("MULTOP")
          match(Token::MULTOP)
          factor()
          ttail()

      # "/" FACTOR TTAIL
      elsif (@lookahead.type == Token::DIVOP)
          found_token("DIVOP")
          match(Token::DIVOP)
          factor()
          ttail()

      # EPSILON
      else
          choose_epsilon("MULTOP", "DIVOP")
      end
      exit_rule("TTAIL")
  end

  # ETAIL      -->   "+" TERM   ETAIL  | "-" TERM   ETAIL | EPSILON
  def etail()
      enter_rule("ETAIL")

      # "+" TERM ETAIL
      if (@lookahead.type == Token::ADDOP)
          found_token("ADDOP")
          match(Token::ADDOP)
          term()
          etail()

      # "-" TERM ETAIL
      elsif (@lookahead.type == Token::SUBOP)
          found_token("SUBOP")
          match(Token::SUBOP)
          term()
          etail()

      # EPSILON
      else
          choose_epsilon("ADDOP", "SUBOP")
      end
      exit_rule("ETAIL")
  end

  # Helper Functions
  def enter_rule(rule)
      puts "Entering #{rule} Rule"
  end

  def exit_rule(rule)
      puts "Exiting #{rule} Rule"
  end

  def found_token(token)
      puts "Found #{token} Token: #{@lookahead.text}"
  end

  def choose_epsilon(*tokens)
      puts "Did not find #{tokens.join(" or ")} Token, choosing EPSILON production"
  end
end
