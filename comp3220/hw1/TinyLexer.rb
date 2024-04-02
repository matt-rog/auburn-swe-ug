#
#  Class Lexer - Reads a TINY program and emits tokens
#
class Lexer
# Constructor - Is passed a file to scan and outputs a token
#               each time nextToken() is invoked.
#   @c        - A one character lookahead 
	def initialize(filename)
		# Need to modify this code so that the program
		# doesn't abend if it can't open the file but rather
		# displays an informative message
		@f = File.open(filename,'r:utf-8')
		
		# Go ahead and read in the first character in the source
		# code file (if there is one) so that you can begin
		# lexing the source code file 
		if (! @f.eof?)
			@c = @f.getc()
		else
			@c = "eof"
			@f.close()
		end
	end
	
	# Method nextCh() returns the next character in the file
	def nextCh()
		if (! @f.eof?)
			@c = @f.getc()
		else
			@c = "eof"
		end
		
		return @c
	end

	# Method nextToken() reads characters in the file and returns
	# the next token
	def nextToken() 
		if @c == "eof"
			return Token.new(Token::EOF,"eof")
				
		elsif (whitespace?(@c))
			return Token.new(Token::WS,parseWhitespace(@c))

		elsif (numeric?(@c))
			return Token.new(Token::INT, parseNumeric(@c))

		elsif (letter?(@c))
			return Token.new(((parseWhitespace(@c) == "print") ? Token::PRINT : Token::ID), parseLetter(@c))

		else

			case @c
			when "="
				token = Token.new(Token::EQUAL,@c)
			when "("
				token = Token.new(Token::LPAREN,@c)
			when ")"
				token = Token.new(Token::RPAREN,@c)
			when "+"
				token = Token.new(Token::PLUS,@c)
			when "-"
				token = Token.new(Token::MINUS,@c)
			when "*"
				token = Token.new(Token::TIMES,@c)
			when "/"
				token = Token.new(Token::SOLIDUS,@c)
			# Bonus lexemes
			when "<"
				token = Token.new(Token::LESS,@c)
			when ">"
				token = Token.new(Token::GREATER,@c)
			when "&"
				token = Token.new(Token::AMP,@c)
			else
				token = Token.new(Token::UNKNWN, @c)
			end
			nextCh()
			return token
		end

	end
	#
	# Helper methods for Scanner
	#
	def letter?(lookAhead)
		lookAhead =~ /^[a-z]|[A-Z]$/
	end

	def numeric?(lookAhead)
		lookAhead =~ /^(\d)+$/
	end

	def whitespace?(lookAhead)
		lookAhead =~ /^(\s)+$/
	end

	#
	# Additional helpers
	#
	def parseLetter(letter)
		str = ""
		while letter?(@c)
			str += @c
			nextCh()
		end
		return str
	end

	def parseNumeric(numeric)
		str = ""
		while numeric?(@c)
			str += @c
			nextCh()
		end
		return str
	end

	def parseWhitespace(whitespace)
		str = ""
		while whitespace?(@c)
			str += @c
			nextCh()
		end
		return str
	end

end