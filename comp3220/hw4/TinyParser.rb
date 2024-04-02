#
#  Parser Class
#
load "TinyLexer.rb"
load "TinyToken.rb"
load "AST.rb"

class Parser < Lexer

    def initialize(filename)
        super(filename)
        consume()
    end

    def consume()
        @lookahead = nextToken()
        while(@lookahead.type == Token::WS)
            @lookahead = nextToken()
        end
    end

    def match(dtype)
        if (@lookahead.type != dtype)
            puts "Expected #{dtype} found #{@lookahead.text}"
			@errors_found+=1
        end
        consume()
    end

    def program()
    	@errors_found = 0
		
		  program_ast = AST.new(Token.new("program","program"))
		
	    while( @lookahead.type != Token::EOF)
        program_ast.addChild(statement())
        end
        
        puts "There were #{@errors_found} parse errors found."
      
		return program_ast
    end

    def statement()
      statement_ast = AST.new(Token.new("statement","statement"))
      if (@lookahead.type == Token::PRINT)
        statement_ast = AST.new(@lookahead)
            match(Token::PRINT)
        statement_ast.addChild(exp())
        else
          statement_ast = assign()
        end
		return statement_ast
    end

    def exp()
      exp_ast = term()
      exp_ops = Array[Token::SUBOP, Token::ADDOP]
      if exp_ops.include?(@lookahead.type)
        temp_exp_ast = exp_ast
        exp_ast = etail()
        exp_ast.setNextChild(temp_exp_ast)
        exp_ast.decrement
      end
      return exp_ast
    end

    def term()
      term_ast = factor()
      term_ops = Array[Token::DIVOP, Token::MULTOP]
      if term_ops.include?(@lookahead.type)
        temp_term_ast = term_ast
        term_ast = ttail()
        term_ast.setNextChild(temp_term_ast)
        term_ast.decrement
        end
      return term_ast
    end

    def factor()
      factor_ast = AST.new(Token.new("factor", "factor"))

      if (@lookahead.type == Token::LPAREN)
            match(Token::LPAREN)
            factor_ast = exp()
            if (@lookahead.type == Token::RPAREN)
                match(Token::RPAREN)
            else
				match(Token::RPAREN)
            end
      elsif (@lookahead.type == Token::INT)
        factor_ast = AST.new(@lookahead)
            match(Token::INT)
      elsif (@lookahead.type == Token::ID)
        factor_ast = AST.new(@lookahead)
        match(Token::ID)
        else
            puts "Expected ( or INT or ID found #{@lookahead.text}"
            @errors_found+=1
            consume()
        end
		return factor_ast
    end

    def ttail()
      ttail_ast = AST.new(Token.new('ttail', 'ttail'))
        if (@lookahead.type == Token::MULTOP)
            ttail_ast = AST.new(@lookahead)
            match(Token::MULTOP)
            ttail_ast = recursive_tail(ttail_ast, method(:factor), method(:ttail))

        elsif (@lookahead.type == Token::DIVOP)
            ttail_ast = AST.new(@lookahead)
            match(Token::DIVOP)
            ttail_ast = recursive_tail(ttail_ast, method(:factor), method(:ttail))

        else
			return nil
        end
      return ttail_ast
    end

    def etail()
      etail_ast = AST.new(Token.new('etail', 'etail'))
        if (@lookahead.type == Token::ADDOP)
          etail_ast = AST.new(@lookahead)
            match(Token::ADDOP)

          etail_ast = recursive_tail(etail_ast, method(:term), method(:etail))

        elsif (@lookahead.type == Token::SUBOP)
          etail_ast = AST.new(@lookahead)
          match(Token::SUBOP)
          etail_ast = recursive_tail(etail_ast, method(:term), method(:etail))

        else
			return nil
        end
      return etail_ast
    end

    def recursive_tail(tail_node, sibling_func, child_func)
      sibling = sibling_func.call()
      tail_node.setNextSibling(sibling)
      child = child_func.call()
      child == nil ? nil : child.addChild(tail_node)

      return tail_node
    end

    def assign()
      assign_ast = AST.new(Token.new("assignment","assignment"))
		if (@lookahead.type == Token::ID)
			idtok = AST.new(@lookahead)
			match(Token::ID)
			if (@lookahead.type == Token::ASSGN)
        assign_ast = AST.new(@lookahead)
        assign_ast.addChild(idtok)
            	match(Token::ASSGN)
        assign_ast.addChild(exp())
        	else
				match(Token::ASSGN)
			end
		else
			match(Token::ID)
        end
		return assign_ast
	end
end
