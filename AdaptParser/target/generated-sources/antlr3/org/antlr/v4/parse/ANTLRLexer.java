// $ANTLR 3.5.3 org/antlr/v4/parse/ANTLRLexer.g 2023-12-20 16:01:28

/*
 [The "BSD licence"]
 Copyright (c) 2005-2009 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.antlr.v4.parse;
import org.antlr.v4.tool.*;
import org.antlr.v4.runtime.misc.Interval;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/** Read in an ANTLR grammar and build an AST.  Try not to do
 *  any actions, just build the tree.
 *
 *  The phases are:
 *
 *		A3Lexer.g (this file)
 *              A3Parser.g
 *              A3Verify.g (derived from A3Walker.g)
 *		assign.types.g
 *		define.g
 *		buildnfa.g
 *		antlr.print.g (optional)
 *		codegen.g
 *
 *  Terence Parr
 *  University of San Francisco
 *  2005
 *  Jim Idle (this v3 grammar)
 *  Temporal Wave LLC
 *  2009
 */
@SuppressWarnings("all")
public class ANTLRLexer extends Lexer {
	public static final int EOF=-1;
	public static final int ACTION=4;
	public static final int ACTION_CHAR_LITERAL=5;
	public static final int ACTION_ESC=6;
	public static final int ACTION_STRING_LITERAL=7;
	public static final int ARG_ACTION=8;
	public static final int ARG_OR_CHARSET=9;
	public static final int ASSIGN=10;
	public static final int AT=11;
	public static final int CATCH=12;
	public static final int CHANNELS=13;
	public static final int COLON=14;
	public static final int COLONCOLON=15;
	public static final int COMMA=16;
	public static final int COMMENT=17;
	public static final int DOC_COMMENT=18;
	public static final int DOLLAR=19;
	public static final int DOT=20;
	public static final int ERRCHAR=21;
	public static final int ESC_SEQ=22;
	public static final int FINALLY=23;
	public static final int FRAGMENT=24;
	public static final int GRAMMAR=25;
	public static final int GT=26;
	public static final int HEX_DIGIT=27;
	public static final int ID=28;
	public static final int IMPORT=29;
	public static final int INT=30;
	public static final int LEXER=31;
	public static final int LEXER_CHAR_SET=32;
	public static final int LOCALS=33;
	public static final int LPAREN=34;
	public static final int LT=35;
	public static final int MODE=36;
	public static final int NESTED_ACTION=37;
	public static final int NLCHARS=38;
	public static final int NOT=39;
	public static final int NameChar=40;
	public static final int NameStartChar=41;
	public static final int OPTIONS=42;
	public static final int OR=43;
	public static final int PARSER=44;
	public static final int PLUS=45;
	public static final int PLUS_ASSIGN=46;
	public static final int POUND=47;
	public static final int QUESTION=48;
	public static final int RANGE=49;
	public static final int RARROW=50;
	public static final int RBRACE=51;
	public static final int RETURNS=52;
	public static final int RPAREN=53;
	public static final int RULE_REF=54;
	public static final int SEMI=55;
	public static final int SEMPRED=56;
	public static final int SRC=57;
	public static final int STAR=58;
	public static final int STRING_LITERAL=59;
	public static final int THROWS=60;
	public static final int TOKENS_SPEC=61;
	public static final int TOKEN_REF=62;
	public static final int UNICODE_ESC=63;
	public static final int UNICODE_EXTENDED_ESC=64;
	public static final int UnicodeBOM=65;
	public static final int WS=66;
	public static final int WSCHARS=67;
	public static final int WSNLCHARS=68;

		public static final int COMMENTS_CHANNEL = 2;

	    public CommonTokenStream tokens; // track stream we push to; need for context info
	    public boolean isLexerRule = false;

		public void grammarError(ErrorType etype, org.antlr.runtime.Token token, Object... args) { }

		/** scan backwards from current point in this.tokens list
		 *  looking for the start of the rule or subrule.
		 *  Return token or null if for some reason we can't find the start.
		 */
		public Token getRuleOrSubruleStartToken() {
			if (tokens == null) return null;
			int i = tokens.index();
			int n = tokens.size();
			if (i >= n) i = n - 1; // seems index == n as we lex
			boolean withinOptionsBlock = false;
			while (i >= 0 && i < n) {
				int ttype = tokens.get(i).getType();
				if (withinOptionsBlock) {
					// Ignore rule options content
					if (ttype == OPTIONS) {
						withinOptionsBlock = false;
					}
				}
				else {
					if (ttype == RBRACE) {
						withinOptionsBlock = true;
					}
					else if (ttype == LPAREN || ttype == TOKEN_REF || ttype == RULE_REF) {
						return tokens.get(i);
					}
				}
				i--;
			}
			return null;
		}


	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public ANTLRLexer() {} 
	public ANTLRLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public ANTLRLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "org/antlr/v4/parse/ANTLRLexer.g"; }

	// $ANTLR start "DOC_COMMENT"
	public final void mDOC_COMMENT() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:204:22: ()
			// org/antlr/v4/parse/ANTLRLexer.g:204:24: 
			{
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOC_COMMENT"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException {
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;


				// Record the start line and offsets as if we need to report an
				// unterminated comment, then we want to show the start of the comment
				// we think is broken, not the end, where people will have to try and work
				// it out themselves.
				//
				int startLine = state.tokenStartLine;
				int offset    = getCharPositionInLine();

			// org/antlr/v4/parse/ANTLRLexer.g:216:5: ( '/' ( '/' ( ( ' $ANTLR' )=> ' $ANTLR' SRC | (~ NLCHARS )* ) | '*' ({...}? => '*' |{...}? =>) ({...}? . )* ( '*/' |) |) )
			// org/antlr/v4/parse/ANTLRLexer.g:219:7: '/' ( '/' ( ( ' $ANTLR' )=> ' $ANTLR' SRC | (~ NLCHARS )* ) | '*' ({...}? => '*' |{...}? =>) ({...}? . )* ( '*/' |) |)
			{
			match('/'); if (state.failed) return;
			// org/antlr/v4/parse/ANTLRLexer.g:221:7: ( '/' ( ( ' $ANTLR' )=> ' $ANTLR' SRC | (~ NLCHARS )* ) | '*' ({...}? => '*' |{...}? =>) ({...}? . )* ( '*/' |) |)
			int alt6=3;
			switch ( input.LA(1) ) {
			case '/':
				{
				alt6=1;
				}
				break;
			case '*':
				{
				alt6=2;
				}
				break;
			default:
				alt6=3;
			}
			switch (alt6) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:227:11: '/' ( ( ' $ANTLR' )=> ' $ANTLR' SRC | (~ NLCHARS )* )
					{
					match('/'); if (state.failed) return;
					// org/antlr/v4/parse/ANTLRLexer.g:228:13: ( ( ' $ANTLR' )=> ' $ANTLR' SRC | (~ NLCHARS )* )
					int alt2=2;
					alt2 = dfa2.predict(input);
					switch (alt2) {
						case 1 :
							// org/antlr/v4/parse/ANTLRLexer.g:229:17: ( ' $ANTLR' )=> ' $ANTLR' SRC
							{
							match(" $ANTLR"); if (state.failed) return;

							mSRC(); if (state.failed) return;

							}
							break;
						case 2 :
							// org/antlr/v4/parse/ANTLRLexer.g:230:17: (~ NLCHARS )*
							{
							// org/antlr/v4/parse/ANTLRLexer.g:230:17: (~ NLCHARS )*
							loop1:
							while (true) {
								int alt1=2;
								int LA1_0 = input.LA(1);
								if ( ((LA1_0 >= '\u0000' && LA1_0 <= '\t')||(LA1_0 >= '\u000B' && LA1_0 <= '\f')||(LA1_0 >= '\u000E' && LA1_0 <= '\uFFFF')) ) {
									alt1=1;
								}

								switch (alt1) {
								case 1 :
									// org/antlr/v4/parse/ANTLRLexer.g:
									{
									if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
										input.consume();
										state.failed=false;
									}
									else {
										if (state.backtracking>0) {state.failed=true; return;}
										MismatchedSetException mse = new MismatchedSetException(null,input);
										recover(mse);
										throw mse;
									}
									}
									break;

								default :
									break loop1;
								}
							}

							}
							break;

					}

					}
					break;
				case 2 :
					// org/antlr/v4/parse/ANTLRLexer.g:237:12: '*' ({...}? => '*' |{...}? =>) ({...}? . )* ( '*/' |)
					{
					match('*'); if (state.failed) return;
					// org/antlr/v4/parse/ANTLRLexer.g:237:16: ({...}? => '*' |{...}? =>)
					int alt3=2;
					int LA3_0 = input.LA(1);
					if ( (LA3_0=='*') && ((( true )||( input.LA(2) != '/')))) {
						int LA3_1 = input.LA(2);
						if ( (( input.LA(2) != '/')) ) {
							alt3=1;
						}
						else if ( (( true )) ) {
							alt3=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 3, 1, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					switch (alt3) {
						case 1 :
							// org/antlr/v4/parse/ANTLRLexer.g:238:17: {...}? => '*'
							{
							if ( !(( input.LA(2) != '/')) ) {
								if (state.backtracking>0) {state.failed=true; return;}
								throw new FailedPredicateException(input, "COMMENT", " input.LA(2) != '/'");
							}
							match('*'); if (state.failed) return;
							if ( state.backtracking==0 ) { _type = DOC_COMMENT; }
							}
							break;
						case 2 :
							// org/antlr/v4/parse/ANTLRLexer.g:239:17: {...}? =>
							{
							if ( !(( true )) ) {
								if (state.backtracking>0) {state.failed=true; return;}
								throw new FailedPredicateException(input, "COMMENT", " true ");
							}
							}
							break;

					}

					// org/antlr/v4/parse/ANTLRLexer.g:244:16: ({...}? . )*
					loop4:
					while (true) {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( (LA4_0=='*') ) {
							int LA4_1 = input.LA(2);
							if ( (LA4_1=='/') ) {
								int LA4_4 = input.LA(3);
								if ( ((    !(input.LA(1) == '*' && input.LA(2) == '/') )) ) {
									alt4=1;
								}

							}
							else {
								alt4=1;
							}

						}
						else if ( ((LA4_0 >= '\u0000' && LA4_0 <= ')')||(LA4_0 >= '+' && LA4_0 <= '\uFFFF')) ) {
							alt4=1;
						}

						switch (alt4) {
						case 1 :
							// org/antlr/v4/parse/ANTLRLexer.g:248:20: {...}? .
							{
							if ( !((    !(input.LA(1) == '*' && input.LA(2) == '/') )) ) {
								if (state.backtracking>0) {state.failed=true; return;}
								throw new FailedPredicateException(input, "COMMENT", "    !(input.LA(1) == '*' && input.LA(2) == '/') ");
							}
							matchAny(); if (state.failed) return;
							}
							break;

						default :
							break loop4;
						}
					}

					// org/antlr/v4/parse/ANTLRLexer.g:255:13: ( '*/' |)
					int alt5=2;
					int LA5_0 = input.LA(1);
					if ( (LA5_0=='*') ) {
						alt5=1;
					}

					else {
						alt5=2;
					}

					switch (alt5) {
						case 1 :
							// org/antlr/v4/parse/ANTLRLexer.g:261:18: '*/'
							{
							match("*/"); if (state.failed) return;

							}
							break;
						case 2 :
							// org/antlr/v4/parse/ANTLRLexer.g:265:18: 
							{
							if ( state.backtracking==0 ) {
							                   // ErrorManager.msg(Msg.UNTERMINATED_DOC_COMMENT, startLine, offset, state.tokenStartCharPositionInLine, startLine, offset, state.tokenStartCharPositionInLine, (Object)null);
							                 }
							}
							break;

					}

					}
					break;
				case 3 :
					// org/antlr/v4/parse/ANTLRLexer.g:273:12: 
					{
					if ( state.backtracking==0 ) {
					           	 // TODO: Insert error message relative to comment start
					             //
					           }
					}
					break;

			}

			if ( state.backtracking==0 ) {
			         // We do not wish to pass the comments in to the parser. If you are
			         // writing a formatter then you will want to preserve the comments off
			         // channel, but could just skip and save token space if not.
			         //
			         _channel=COMMENTS_CHANNEL;
			       }
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMENT"

	// $ANTLR start "ARG_OR_CHARSET"
	public final void mARG_OR_CHARSET() throws RecognitionException {
		try {
			int _type = ARG_OR_CHARSET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:289:5: ({...}? => LEXER_CHAR_SET |{...}? => ARG_ACTION )
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0=='[') && (((!isLexerRule)||(isLexerRule)))) {
				int LA7_1 = input.LA(2);
				if ( ((isLexerRule)) ) {
					alt7=1;
				}
				else if ( ((!isLexerRule)) ) {
					alt7=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 7, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			switch (alt7) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:289:9: {...}? => LEXER_CHAR_SET
					{
					if ( !((isLexerRule)) ) {
						if (state.backtracking>0) {state.failed=true; return;}
						throw new FailedPredicateException(input, "ARG_OR_CHARSET", "isLexerRule");
					}
					mLEXER_CHAR_SET(); if (state.failed) return;

					if ( state.backtracking==0 ) {_type=LEXER_CHAR_SET;}
					}
					break;
				case 2 :
					// org/antlr/v4/parse/ANTLRLexer.g:290:9: {...}? => ARG_ACTION
					{
					if ( !((!isLexerRule)) ) {
						if (state.backtracking>0) {state.failed=true; return;}
						throw new FailedPredicateException(input, "ARG_OR_CHARSET", "!isLexerRule");
					}
					mARG_ACTION(); if (state.failed) return;

					if ( state.backtracking==0 ) {
					        _type=ARG_ACTION;
					        // Set the token text to our gathered string minus outer [ ]
					        String t = getText();
					        t = t.substring(1,t.length()-1);
					        setText(t);
					        }
					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ARG_OR_CHARSET"

	// $ANTLR start "LEXER_CHAR_SET"
	public final void mLEXER_CHAR_SET() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:302:2: ( '[' ( '\\\\' ~ ( '\\r' | '\\n' ) |~ ( '\\r' | '\\n' | '\\\\' | ']' ) )* ']' )
			// org/antlr/v4/parse/ANTLRLexer.g:302:4: '[' ( '\\\\' ~ ( '\\r' | '\\n' ) |~ ( '\\r' | '\\n' | '\\\\' | ']' ) )* ']'
			{
			match('['); if (state.failed) return;
			// org/antlr/v4/parse/ANTLRLexer.g:303:3: ( '\\\\' ~ ( '\\r' | '\\n' ) |~ ( '\\r' | '\\n' | '\\\\' | ']' ) )*
			loop8:
			while (true) {
				int alt8=3;
				int LA8_0 = input.LA(1);
				if ( (LA8_0=='\\') ) {
					alt8=1;
				}
				else if ( ((LA8_0 >= '\u0000' && LA8_0 <= '\t')||(LA8_0 >= '\u000B' && LA8_0 <= '\f')||(LA8_0 >= '\u000E' && LA8_0 <= '[')||(LA8_0 >= '^' && LA8_0 <= '\uFFFF')) ) {
					alt8=2;
				}

				switch (alt8) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:303:5: '\\\\' ~ ( '\\r' | '\\n' )
					{
					match('\\'); if (state.failed) return;
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;
				case 2 :
					// org/antlr/v4/parse/ANTLRLexer.g:304:5: ~ ( '\\r' | '\\n' | '\\\\' | ']' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '[')||(input.LA(1) >= '^' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop8;
				}
			}

			match(']'); if (state.failed) return;
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LEXER_CHAR_SET"

	// $ANTLR start "ARG_ACTION"
	public final void mARG_ACTION() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:319:2: ( '[' ( ARG_ACTION | ( '\"' )=> ACTION_STRING_LITERAL | ( '\\'' )=> ACTION_CHAR_LITERAL |~ ( '[' | ']' ) )* ']' )
			// org/antlr/v4/parse/ANTLRLexer.g:319:4: '[' ( ARG_ACTION | ( '\"' )=> ACTION_STRING_LITERAL | ( '\\'' )=> ACTION_CHAR_LITERAL |~ ( '[' | ']' ) )* ']'
			{
			match('['); if (state.failed) return;
			// org/antlr/v4/parse/ANTLRLexer.g:320:10: ( ARG_ACTION | ( '\"' )=> ACTION_STRING_LITERAL | ( '\\'' )=> ACTION_CHAR_LITERAL |~ ( '[' | ']' ) )*
			loop9:
			while (true) {
				int alt9=5;
				int LA9_0 = input.LA(1);
				if ( (LA9_0=='[') ) {
					alt9=1;
				}
				else if ( (LA9_0=='\"') ) {
					int LA9_3 = input.LA(2);
					if ( (synpred2_ANTLRLexer()) ) {
						alt9=2;
					}
					else if ( (true) ) {
						alt9=4;
					}

				}
				else if ( (LA9_0=='\'') ) {
					int LA9_4 = input.LA(2);
					if ( (synpred3_ANTLRLexer()) ) {
						alt9=3;
					}
					else if ( (true) ) {
						alt9=4;
					}

				}
				else if ( ((LA9_0 >= '\u0000' && LA9_0 <= '!')||(LA9_0 >= '#' && LA9_0 <= '&')||(LA9_0 >= '(' && LA9_0 <= 'Z')||LA9_0=='\\'||(LA9_0 >= '^' && LA9_0 <= '\uFFFF')) ) {
					alt9=4;
				}

				switch (alt9) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:321:14: ARG_ACTION
					{
					mARG_ACTION(); if (state.failed) return;

					}
					break;
				case 2 :
					// org/antlr/v4/parse/ANTLRLexer.g:323:14: ( '\"' )=> ACTION_STRING_LITERAL
					{
					mACTION_STRING_LITERAL(); if (state.failed) return;

					}
					break;
				case 3 :
					// org/antlr/v4/parse/ANTLRLexer.g:325:14: ( '\\'' )=> ACTION_CHAR_LITERAL
					{
					mACTION_CHAR_LITERAL(); if (state.failed) return;

					}
					break;
				case 4 :
					// org/antlr/v4/parse/ANTLRLexer.g:327:14: ~ ( '[' | ']' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= 'Z')||input.LA(1)=='\\'||(input.LA(1) >= '^' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop9;
				}
			}

			match(']'); if (state.failed) return;
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ARG_ACTION"

	// $ANTLR start "ACTION"
	public final void mACTION() throws RecognitionException {
		try {
			int _type = ACTION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:345:2: ( NESTED_ACTION ( '?' )? )
			// org/antlr/v4/parse/ANTLRLexer.g:345:4: NESTED_ACTION ( '?' )?
			{
			mNESTED_ACTION(); if (state.failed) return;

			// org/antlr/v4/parse/ANTLRLexer.g:345:18: ( '?' )?
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0=='?') ) {
				alt10=1;
			}
			switch (alt10) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:345:20: '?'
					{
					match('?'); if (state.failed) return;
					if ( state.backtracking==0 ) {_type = SEMPRED;}
					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ACTION"

	// $ANTLR start "NESTED_ACTION"
	public final void mNESTED_ACTION() throws RecognitionException {
		try {


				// Record the start line and offsets as if we need to report an
				// unterminated block, then we want to show the start of the comment
				// we think is broken, not the end, where people will have to try and work
				// it out themselves.
				//
				int startLine = getLine();
				int offset    = getCharPositionInLine();

			// org/antlr/v4/parse/ANTLRLexer.g:372:5: ( '{' ( NESTED_ACTION | ACTION_CHAR_LITERAL | COMMENT | ACTION_STRING_LITERAL | ACTION_ESC |~ ( '\\\\' | '\"' | '\\'' | '/' | '{' | '}' ) )* ( '}' |) )
			// org/antlr/v4/parse/ANTLRLexer.g:374:4: '{' ( NESTED_ACTION | ACTION_CHAR_LITERAL | COMMENT | ACTION_STRING_LITERAL | ACTION_ESC |~ ( '\\\\' | '\"' | '\\'' | '/' | '{' | '}' ) )* ( '}' |)
			{
			match('{'); if (state.failed) return;
			// org/antlr/v4/parse/ANTLRLexer.g:375:7: ( NESTED_ACTION | ACTION_CHAR_LITERAL | COMMENT | ACTION_STRING_LITERAL | ACTION_ESC |~ ( '\\\\' | '\"' | '\\'' | '/' | '{' | '}' ) )*
			loop11:
			while (true) {
				int alt11=7;
				int LA11_0 = input.LA(1);
				if ( (LA11_0=='{') ) {
					alt11=1;
				}
				else if ( (LA11_0=='\'') ) {
					alt11=2;
				}
				else if ( (LA11_0=='/') ) {
					alt11=3;
				}
				else if ( (LA11_0=='\"') ) {
					alt11=4;
				}
				else if ( (LA11_0=='\\') ) {
					alt11=5;
				}
				else if ( ((LA11_0 >= '\u0000' && LA11_0 <= '!')||(LA11_0 >= '#' && LA11_0 <= '&')||(LA11_0 >= '(' && LA11_0 <= '.')||(LA11_0 >= '0' && LA11_0 <= '[')||(LA11_0 >= ']' && LA11_0 <= 'z')||LA11_0=='|'||(LA11_0 >= '~' && LA11_0 <= '\uFFFF')) ) {
					alt11=6;
				}

				switch (alt11) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:390:8: NESTED_ACTION
					{
					mNESTED_ACTION(); if (state.failed) return;

					}
					break;
				case 2 :
					// org/antlr/v4/parse/ANTLRLexer.g:394:11: ACTION_CHAR_LITERAL
					{
					mACTION_CHAR_LITERAL(); if (state.failed) return;

					}
					break;
				case 3 :
					// org/antlr/v4/parse/ANTLRLexer.g:399:11: COMMENT
					{
					mCOMMENT(); if (state.failed) return;

					}
					break;
				case 4 :
					// org/antlr/v4/parse/ANTLRLexer.g:403:11: ACTION_STRING_LITERAL
					{
					mACTION_STRING_LITERAL(); if (state.failed) return;

					}
					break;
				case 5 :
					// org/antlr/v4/parse/ANTLRLexer.g:407:8: ACTION_ESC
					{
					mACTION_ESC(); if (state.failed) return;

					}
					break;
				case 6 :
					// org/antlr/v4/parse/ANTLRLexer.g:412:8: ~ ( '\\\\' | '\"' | '\\'' | '/' | '{' | '}' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= 'z')||input.LA(1)=='|'||(input.LA(1) >= '~' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop11;
				}
			}

			// org/antlr/v4/parse/ANTLRLexer.g:416:2: ( '}' |)
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0=='}') ) {
				alt12=1;
			}

			else {
				alt12=2;
			}

			switch (alt12) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:419:6: '}'
					{
					match('}'); if (state.failed) return;
					}
					break;
				case 2 :
					// org/antlr/v4/parse/ANTLRLexer.g:424:6: 
					{
					if ( state.backtracking==0 ) {
						        // TODO: Report imbalanced {}
						        System.out.println("Block starting  at line " + startLine + " offset " + (offset+1) + " contains imbalanced {} or is missing a }");
						    }
					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NESTED_ACTION"

	// $ANTLR start "OPTIONS"
	public final void mOPTIONS() throws RecognitionException {
		try {
			int _type = OPTIONS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:440:14: ( 'options' ( WSNLCHARS )* '{' )
			// org/antlr/v4/parse/ANTLRLexer.g:440:16: 'options' ( WSNLCHARS )* '{'
			{
			match("options"); if (state.failed) return;

			// org/antlr/v4/parse/ANTLRLexer.g:440:27: ( WSNLCHARS )*
			loop13:
			while (true) {
				int alt13=2;
				int LA13_0 = input.LA(1);
				if ( ((LA13_0 >= '\t' && LA13_0 <= '\n')||(LA13_0 >= '\f' && LA13_0 <= '\r')||LA13_0==' ') ) {
					alt13=1;
				}

				switch (alt13) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop13;
				}
			}

			match('{'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OPTIONS"

	// $ANTLR start "TOKENS_SPEC"
	public final void mTOKENS_SPEC() throws RecognitionException {
		try {
			int _type = TOKENS_SPEC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:441:14: ( 'tokens' ( WSNLCHARS )* '{' )
			// org/antlr/v4/parse/ANTLRLexer.g:441:16: 'tokens' ( WSNLCHARS )* '{'
			{
			match("tokens"); if (state.failed) return;

			// org/antlr/v4/parse/ANTLRLexer.g:441:27: ( WSNLCHARS )*
			loop14:
			while (true) {
				int alt14=2;
				int LA14_0 = input.LA(1);
				if ( ((LA14_0 >= '\t' && LA14_0 <= '\n')||(LA14_0 >= '\f' && LA14_0 <= '\r')||LA14_0==' ') ) {
					alt14=1;
				}

				switch (alt14) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop14;
				}
			}

			match('{'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TOKENS_SPEC"

	// $ANTLR start "CHANNELS"
	public final void mCHANNELS() throws RecognitionException {
		try {
			int _type = CHANNELS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:442:14: ( 'channels' ( WSNLCHARS )* '{' )
			// org/antlr/v4/parse/ANTLRLexer.g:442:16: 'channels' ( WSNLCHARS )* '{'
			{
			match("channels"); if (state.failed) return;

			// org/antlr/v4/parse/ANTLRLexer.g:442:27: ( WSNLCHARS )*
			loop15:
			while (true) {
				int alt15=2;
				int LA15_0 = input.LA(1);
				if ( ((LA15_0 >= '\t' && LA15_0 <= '\n')||(LA15_0 >= '\f' && LA15_0 <= '\r')||LA15_0==' ') ) {
					alt15=1;
				}

				switch (alt15) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop15;
				}
			}

			match('{'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CHANNELS"

	// $ANTLR start "IMPORT"
	public final void mIMPORT() throws RecognitionException {
		try {
			int _type = IMPORT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:444:14: ( 'import' )
			// org/antlr/v4/parse/ANTLRLexer.g:444:16: 'import'
			{
			match("import"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IMPORT"

	// $ANTLR start "FRAGMENT"
	public final void mFRAGMENT() throws RecognitionException {
		try {
			int _type = FRAGMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:445:14: ( 'fragment' )
			// org/antlr/v4/parse/ANTLRLexer.g:445:16: 'fragment'
			{
			match("fragment"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FRAGMENT"

	// $ANTLR start "LEXER"
	public final void mLEXER() throws RecognitionException {
		try {
			int _type = LEXER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:446:14: ( 'lexer' )
			// org/antlr/v4/parse/ANTLRLexer.g:446:16: 'lexer'
			{
			match("lexer"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LEXER"

	// $ANTLR start "PARSER"
	public final void mPARSER() throws RecognitionException {
		try {
			int _type = PARSER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:447:14: ( 'parser' )
			// org/antlr/v4/parse/ANTLRLexer.g:447:16: 'parser'
			{
			match("parser"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PARSER"

	// $ANTLR start "GRAMMAR"
	public final void mGRAMMAR() throws RecognitionException {
		try {
			int _type = GRAMMAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:448:14: ( 'grammar' )
			// org/antlr/v4/parse/ANTLRLexer.g:448:16: 'grammar'
			{
			match("grammar"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GRAMMAR"

	// $ANTLR start "RETURNS"
	public final void mRETURNS() throws RecognitionException {
		try {
			int _type = RETURNS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:449:14: ( 'returns' )
			// org/antlr/v4/parse/ANTLRLexer.g:449:16: 'returns'
			{
			match("returns"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RETURNS"

	// $ANTLR start "LOCALS"
	public final void mLOCALS() throws RecognitionException {
		try {
			int _type = LOCALS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:450:14: ( 'locals' )
			// org/antlr/v4/parse/ANTLRLexer.g:450:16: 'locals'
			{
			match("locals"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LOCALS"

	// $ANTLR start "THROWS"
	public final void mTHROWS() throws RecognitionException {
		try {
			int _type = THROWS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:451:14: ( 'throws' )
			// org/antlr/v4/parse/ANTLRLexer.g:451:16: 'throws'
			{
			match("throws"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "THROWS"

	// $ANTLR start "CATCH"
	public final void mCATCH() throws RecognitionException {
		try {
			int _type = CATCH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:452:14: ( 'catch' )
			// org/antlr/v4/parse/ANTLRLexer.g:452:16: 'catch'
			{
			match("catch"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CATCH"

	// $ANTLR start "FINALLY"
	public final void mFINALLY() throws RecognitionException {
		try {
			int _type = FINALLY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:453:14: ( 'finally' )
			// org/antlr/v4/parse/ANTLRLexer.g:453:16: 'finally'
			{
			match("finally"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FINALLY"

	// $ANTLR start "MODE"
	public final void mMODE() throws RecognitionException {
		try {
			int _type = MODE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:454:14: ( 'mode' )
			// org/antlr/v4/parse/ANTLRLexer.g:454:16: 'mode'
			{
			match("mode"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MODE"

	// $ANTLR start "COLON"
	public final void mCOLON() throws RecognitionException {
		try {
			int _type = COLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:461:14: ( ':' )
			// org/antlr/v4/parse/ANTLRLexer.g:461:16: ':'
			{
			match(':'); if (state.failed) return;
			if ( state.backtracking==0 ) {
			               // scan backwards, looking for a RULE_REF or TOKEN_REF.
			               // which would indicate the start of a rule definition.
			               // If we see a LPAREN, then it's the start of the subrule.
			               // this.tokens is the token string we are pushing into, so
			               // just loop backwards looking for a rule definition. Then
			               // we set isLexerRule.
			               Token t = getRuleOrSubruleStartToken();
			               if ( t!=null ) {
			                    if ( t.getType()==RULE_REF ) isLexerRule = false;
			                    else if ( t.getType()==TOKEN_REF ) isLexerRule = true;
			                    // else must be subrule; don't alter context
			               }
			               }
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COLON"

	// $ANTLR start "COLONCOLON"
	public final void mCOLONCOLON() throws RecognitionException {
		try {
			int _type = COLONCOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:477:14: ( '::' )
			// org/antlr/v4/parse/ANTLRLexer.g:477:16: '::'
			{
			match("::"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COLONCOLON"

	// $ANTLR start "COMMA"
	public final void mCOMMA() throws RecognitionException {
		try {
			int _type = COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:478:14: ( ',' )
			// org/antlr/v4/parse/ANTLRLexer.g:478:16: ','
			{
			match(','); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMA"

	// $ANTLR start "SEMI"
	public final void mSEMI() throws RecognitionException {
		try {
			int _type = SEMI;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:479:14: ( ';' )
			// org/antlr/v4/parse/ANTLRLexer.g:479:16: ';'
			{
			match(';'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SEMI"

	// $ANTLR start "LPAREN"
	public final void mLPAREN() throws RecognitionException {
		try {
			int _type = LPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:480:14: ( '(' )
			// org/antlr/v4/parse/ANTLRLexer.g:480:16: '('
			{
			match('('); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LPAREN"

	// $ANTLR start "RPAREN"
	public final void mRPAREN() throws RecognitionException {
		try {
			int _type = RPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:481:14: ( ')' )
			// org/antlr/v4/parse/ANTLRLexer.g:481:16: ')'
			{
			match(')'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RPAREN"

	// $ANTLR start "RARROW"
	public final void mRARROW() throws RecognitionException {
		try {
			int _type = RARROW;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:482:14: ( '->' )
			// org/antlr/v4/parse/ANTLRLexer.g:482:16: '->'
			{
			match("->"); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RARROW"

	// $ANTLR start "LT"
	public final void mLT() throws RecognitionException {
		try {
			int _type = LT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:483:14: ( '<' )
			// org/antlr/v4/parse/ANTLRLexer.g:483:16: '<'
			{
			match('<'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LT"

	// $ANTLR start "GT"
	public final void mGT() throws RecognitionException {
		try {
			int _type = GT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:484:14: ( '>' )
			// org/antlr/v4/parse/ANTLRLexer.g:484:16: '>'
			{
			match('>'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GT"

	// $ANTLR start "ASSIGN"
	public final void mASSIGN() throws RecognitionException {
		try {
			int _type = ASSIGN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:485:14: ( '=' )
			// org/antlr/v4/parse/ANTLRLexer.g:485:16: '='
			{
			match('='); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ASSIGN"

	// $ANTLR start "QUESTION"
	public final void mQUESTION() throws RecognitionException {
		try {
			int _type = QUESTION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:486:14: ( '?' )
			// org/antlr/v4/parse/ANTLRLexer.g:486:16: '?'
			{
			match('?'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "QUESTION"

	// $ANTLR start "STAR"
	public final void mSTAR() throws RecognitionException {
		try {
			int _type = STAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:487:14: ( '*' )
			// org/antlr/v4/parse/ANTLRLexer.g:487:16: '*'
			{
			match('*'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STAR"

	// $ANTLR start "PLUS"
	public final void mPLUS() throws RecognitionException {
		try {
			int _type = PLUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:488:14: ( '+' )
			// org/antlr/v4/parse/ANTLRLexer.g:488:16: '+'
			{
			match('+'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PLUS"

	// $ANTLR start "PLUS_ASSIGN"
	public final void mPLUS_ASSIGN() throws RecognitionException {
		try {
			int _type = PLUS_ASSIGN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:489:14: ( '+=' )
			// org/antlr/v4/parse/ANTLRLexer.g:489:16: '+='
			{
			match("+="); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PLUS_ASSIGN"

	// $ANTLR start "OR"
	public final void mOR() throws RecognitionException {
		try {
			int _type = OR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:490:14: ( '|' )
			// org/antlr/v4/parse/ANTLRLexer.g:490:16: '|'
			{
			match('|'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OR"

	// $ANTLR start "DOLLAR"
	public final void mDOLLAR() throws RecognitionException {
		try {
			int _type = DOLLAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:491:14: ( '$' )
			// org/antlr/v4/parse/ANTLRLexer.g:491:16: '$'
			{
			match('$'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOLLAR"

	// $ANTLR start "DOT"
	public final void mDOT() throws RecognitionException {
		try {
			int _type = DOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:492:11: ( '.' )
			// org/antlr/v4/parse/ANTLRLexer.g:492:13: '.'
			{
			match('.'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOT"

	// $ANTLR start "RANGE"
	public final void mRANGE() throws RecognitionException {
		try {
			int _type = RANGE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:493:14: ( '..' )
			// org/antlr/v4/parse/ANTLRLexer.g:493:16: '..'
			{
			match(".."); if (state.failed) return;

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RANGE"

	// $ANTLR start "AT"
	public final void mAT() throws RecognitionException {
		try {
			int _type = AT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:494:14: ( '@' )
			// org/antlr/v4/parse/ANTLRLexer.g:494:16: '@'
			{
			match('@'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AT"

	// $ANTLR start "POUND"
	public final void mPOUND() throws RecognitionException {
		try {
			int _type = POUND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:495:14: ( '#' )
			// org/antlr/v4/parse/ANTLRLexer.g:495:16: '#'
			{
			match('#'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "POUND"

	// $ANTLR start "NOT"
	public final void mNOT() throws RecognitionException {
		try {
			int _type = NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:496:14: ( '~' )
			// org/antlr/v4/parse/ANTLRLexer.g:496:16: '~'
			{
			match('~'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NOT"

	// $ANTLR start "RBRACE"
	public final void mRBRACE() throws RecognitionException {
		try {
			int _type = RBRACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:497:14: ( '}' )
			// org/antlr/v4/parse/ANTLRLexer.g:497:16: '}'
			{
			match('}'); if (state.failed) return;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RBRACE"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			CommonToken a=null;

			// org/antlr/v4/parse/ANTLRLexer.g:500:6: (a= NameStartChar ( NameChar )* )
			// org/antlr/v4/parse/ANTLRLexer.g:500:8: a= NameStartChar ( NameChar )*
			{
			int aStart2773 = getCharIndex();
			int aStartLine2773 = getLine();
			int aStartCharPos2773 = getCharPositionInLine();
			mNameStartChar(); if (state.failed) return;
			a = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, aStart2773, getCharIndex()-1);
			a.setLine(aStartLine2773);
			a.setCharPositionInLine(aStartCharPos2773);

			// org/antlr/v4/parse/ANTLRLexer.g:500:24: ( NameChar )*
			loop16:
			while (true) {
				int alt16=2;
				int LA16_0 = input.LA(1);
				if ( ((LA16_0 >= '0' && LA16_0 <= '9')||(LA16_0 >= 'A' && LA16_0 <= 'Z')||LA16_0=='_'||(LA16_0 >= 'a' && LA16_0 <= 'z')||LA16_0=='\u00B7'||(LA16_0 >= '\u00C0' && LA16_0 <= '\u00D6')||(LA16_0 >= '\u00D8' && LA16_0 <= '\u00F6')||(LA16_0 >= '\u00F8' && LA16_0 <= '\u037D')||(LA16_0 >= '\u037F' && LA16_0 <= '\u1FFF')||(LA16_0 >= '\u200C' && LA16_0 <= '\u200D')||(LA16_0 >= '\u203F' && LA16_0 <= '\u2040')||(LA16_0 >= '\u2070' && LA16_0 <= '\u218F')||(LA16_0 >= '\u2C00' && LA16_0 <= '\u2FEF')||(LA16_0 >= '\u3001' && LA16_0 <= '\uD7FF')||(LA16_0 >= '\uF900' && LA16_0 <= '\uFDCF')||(LA16_0 >= '\uFDF0' && LA16_0 <= '\uFEFE')||(LA16_0 >= '\uFF00' && LA16_0 <= '\uFFFD')) ) {
					alt16=1;
				}

				switch (alt16) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z')||input.LA(1)=='\u00B7'||(input.LA(1) >= '\u00C0' && input.LA(1) <= '\u00D6')||(input.LA(1) >= '\u00D8' && input.LA(1) <= '\u00F6')||(input.LA(1) >= '\u00F8' && input.LA(1) <= '\u037D')||(input.LA(1) >= '\u037F' && input.LA(1) <= '\u1FFF')||(input.LA(1) >= '\u200C' && input.LA(1) <= '\u200D')||(input.LA(1) >= '\u203F' && input.LA(1) <= '\u2040')||(input.LA(1) >= '\u2070' && input.LA(1) <= '\u218F')||(input.LA(1) >= '\u2C00' && input.LA(1) <= '\u2FEF')||(input.LA(1) >= '\u3001' && input.LA(1) <= '\uD7FF')||(input.LA(1) >= '\uF900' && input.LA(1) <= '\uFDCF')||(input.LA(1) >= '\uFDF0' && input.LA(1) <= '\uFEFE')||(input.LA(1) >= '\uFF00' && input.LA(1) <= '\uFFFD') ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop16;
				}
			}

			if ( state.backtracking==0 ) {
							if ( Grammar.isTokenName((a!=null?a.getText():null)) ) _type = TOKEN_REF;
							else _type = RULE_REF;
							}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ID"

	// $ANTLR start "NameChar"
	public final void mNameChar() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:508:13: ( NameStartChar | '0' .. '9' | '_' | '\\u00B7' | '\\u0300' .. '\\u036F' | '\\u203F' .. '\\u2040' )
			// org/antlr/v4/parse/ANTLRLexer.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z')||input.LA(1)=='\u00B7'||(input.LA(1) >= '\u00C0' && input.LA(1) <= '\u00D6')||(input.LA(1) >= '\u00D8' && input.LA(1) <= '\u00F6')||(input.LA(1) >= '\u00F8' && input.LA(1) <= '\u037D')||(input.LA(1) >= '\u037F' && input.LA(1) <= '\u1FFF')||(input.LA(1) >= '\u200C' && input.LA(1) <= '\u200D')||(input.LA(1) >= '\u203F' && input.LA(1) <= '\u2040')||(input.LA(1) >= '\u2070' && input.LA(1) <= '\u218F')||(input.LA(1) >= '\u2C00' && input.LA(1) <= '\u2FEF')||(input.LA(1) >= '\u3001' && input.LA(1) <= '\uD7FF')||(input.LA(1) >= '\uF900' && input.LA(1) <= '\uFDCF')||(input.LA(1) >= '\uFDF0' && input.LA(1) <= '\uFEFE')||(input.LA(1) >= '\uFF00' && input.LA(1) <= '\uFFFD') ) {
				input.consume();
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NameChar"

	// $ANTLR start "NameStartChar"
	public final void mNameStartChar() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:518:13: ( 'A' .. 'Z' | 'a' .. 'z' | '\\u00C0' .. '\\u00D6' | '\\u00D8' .. '\\u00F6' | '\\u00F8' .. '\\u02FF' | '\\u0370' .. '\\u037D' | '\\u037F' .. '\\u1FFF' | '\\u200C' .. '\\u200D' | '\\u2070' .. '\\u218F' | '\\u2C00' .. '\\u2FEF' | '\\u3001' .. '\\uD7FF' | '\\uF900' .. '\\uFDCF' | '\\uFDF0' .. '\\uFEFE' | '\\uFF00' .. '\\uFFFD' )
			// org/antlr/v4/parse/ANTLRLexer.g:
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z')||(input.LA(1) >= '\u00C0' && input.LA(1) <= '\u00D6')||(input.LA(1) >= '\u00D8' && input.LA(1) <= '\u00F6')||(input.LA(1) >= '\u00F8' && input.LA(1) <= '\u02FF')||(input.LA(1) >= '\u0370' && input.LA(1) <= '\u037D')||(input.LA(1) >= '\u037F' && input.LA(1) <= '\u1FFF')||(input.LA(1) >= '\u200C' && input.LA(1) <= '\u200D')||(input.LA(1) >= '\u2070' && input.LA(1) <= '\u218F')||(input.LA(1) >= '\u2C00' && input.LA(1) <= '\u2FEF')||(input.LA(1) >= '\u3001' && input.LA(1) <= '\uD7FF')||(input.LA(1) >= '\uF900' && input.LA(1) <= '\uFDCF')||(input.LA(1) >= '\uFDF0' && input.LA(1) <= '\uFEFE')||(input.LA(1) >= '\uFF00' && input.LA(1) <= '\uFFFD') ) {
				input.consume();
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NameStartChar"

	// $ANTLR start "ACTION_CHAR_LITERAL"
	public final void mACTION_CHAR_LITERAL() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:552:2: ( '\\'' ( ( '\\\\' )=> ACTION_ESC |~ '\\'' )* '\\'' )
			// org/antlr/v4/parse/ANTLRLexer.g:552:4: '\\'' ( ( '\\\\' )=> ACTION_ESC |~ '\\'' )* '\\''
			{
			match('\''); if (state.failed) return;
			// org/antlr/v4/parse/ANTLRLexer.g:552:9: ( ( '\\\\' )=> ACTION_ESC |~ '\\'' )*
			loop17:
			while (true) {
				int alt17=3;
				int LA17_0 = input.LA(1);
				if ( (LA17_0=='\\') ) {
					int LA17_2 = input.LA(2);
					if ( (LA17_2=='\'') ) {
						int LA17_4 = input.LA(3);
						if ( (LA17_4=='\'') && (synpred4_ANTLRLexer())) {
							alt17=1;
						}
						else if ( (LA17_4=='\\') && (synpred4_ANTLRLexer())) {
							alt17=1;
						}
						else if ( ((LA17_4 >= '\u0000' && LA17_4 <= '&')||(LA17_4 >= '(' && LA17_4 <= '[')||(LA17_4 >= ']' && LA17_4 <= '\uFFFF')) && (synpred4_ANTLRLexer())) {
							alt17=1;
						}
						else {
							alt17=2;
						}

					}
					else if ( (LA17_2=='\\') ) {
						int LA17_5 = input.LA(3);
						if ( (synpred4_ANTLRLexer()) ) {
							alt17=1;
						}
						else if ( (true) ) {
							alt17=2;
						}

					}
					else if ( ((LA17_2 >= '\u0000' && LA17_2 <= '&')||(LA17_2 >= '(' && LA17_2 <= '[')||(LA17_2 >= ']' && LA17_2 <= '\uFFFF')) ) {
						int LA17_6 = input.LA(3);
						if ( (synpred4_ANTLRLexer()) ) {
							alt17=1;
						}
						else if ( (true) ) {
							alt17=2;
						}

					}

				}
				else if ( ((LA17_0 >= '\u0000' && LA17_0 <= '&')||(LA17_0 >= '(' && LA17_0 <= '[')||(LA17_0 >= ']' && LA17_0 <= '\uFFFF')) ) {
					alt17=2;
				}

				switch (alt17) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:552:10: ( '\\\\' )=> ACTION_ESC
					{
					mACTION_ESC(); if (state.failed) return;

					}
					break;
				case 2 :
					// org/antlr/v4/parse/ANTLRLexer.g:552:31: ~ '\\''
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop17;
				}
			}

			match('\''); if (state.failed) return;
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ACTION_CHAR_LITERAL"

	// $ANTLR start "ACTION_STRING_LITERAL"
	public final void mACTION_STRING_LITERAL() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:562:2: ( '\"' ( ( '\\\\' )=> ACTION_ESC |~ '\"' )* '\"' )
			// org/antlr/v4/parse/ANTLRLexer.g:562:4: '\"' ( ( '\\\\' )=> ACTION_ESC |~ '\"' )* '\"'
			{
			match('\"'); if (state.failed) return;
			// org/antlr/v4/parse/ANTLRLexer.g:562:8: ( ( '\\\\' )=> ACTION_ESC |~ '\"' )*
			loop18:
			while (true) {
				int alt18=3;
				int LA18_0 = input.LA(1);
				if ( (LA18_0=='\\') ) {
					int LA18_2 = input.LA(2);
					if ( (LA18_2=='\"') ) {
						int LA18_4 = input.LA(3);
						if ( (LA18_4=='\"') && (synpred5_ANTLRLexer())) {
							alt18=1;
						}
						else if ( (LA18_4=='\\') && (synpred5_ANTLRLexer())) {
							alt18=1;
						}
						else if ( ((LA18_4 >= '\u0000' && LA18_4 <= '!')||(LA18_4 >= '#' && LA18_4 <= '[')||(LA18_4 >= ']' && LA18_4 <= '\uFFFF')) && (synpred5_ANTLRLexer())) {
							alt18=1;
						}
						else {
							alt18=2;
						}

					}
					else if ( (LA18_2=='\\') ) {
						int LA18_5 = input.LA(3);
						if ( (synpred5_ANTLRLexer()) ) {
							alt18=1;
						}
						else if ( (true) ) {
							alt18=2;
						}

					}
					else if ( ((LA18_2 >= '\u0000' && LA18_2 <= '!')||(LA18_2 >= '#' && LA18_2 <= '[')||(LA18_2 >= ']' && LA18_2 <= '\uFFFF')) ) {
						int LA18_6 = input.LA(3);
						if ( (synpred5_ANTLRLexer()) ) {
							alt18=1;
						}
						else if ( (true) ) {
							alt18=2;
						}

					}

				}
				else if ( ((LA18_0 >= '\u0000' && LA18_0 <= '!')||(LA18_0 >= '#' && LA18_0 <= '[')||(LA18_0 >= ']' && LA18_0 <= '\uFFFF')) ) {
					alt18=2;
				}

				switch (alt18) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:562:9: ( '\\\\' )=> ACTION_ESC
					{
					mACTION_ESC(); if (state.failed) return;

					}
					break;
				case 2 :
					// org/antlr/v4/parse/ANTLRLexer.g:562:30: ~ '\"'
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop18;
				}
			}

			match('\"'); if (state.failed) return;
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ACTION_STRING_LITERAL"

	// $ANTLR start "ACTION_ESC"
	public final void mACTION_ESC() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:572:2: ( '\\\\' . )
			// org/antlr/v4/parse/ANTLRLexer.g:572:4: '\\\\' .
			{
			match('\\'); if (state.failed) return;
			matchAny(); if (state.failed) return;
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ACTION_ESC"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:580:5: ( ( '0' .. '9' )+ )
			// org/antlr/v4/parse/ANTLRLexer.g:580:7: ( '0' .. '9' )+
			{
			// org/antlr/v4/parse/ANTLRLexer.g:580:7: ( '0' .. '9' )+
			int cnt19=0;
			loop19:
			while (true) {
				int alt19=2;
				int LA19_0 = input.LA(1);
				if ( ((LA19_0 >= '0' && LA19_0 <= '9')) ) {
					alt19=1;
				}

				switch (alt19) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt19 >= 1 ) break loop19;
					if (state.backtracking>0) {state.failed=true; return;}
					EarlyExitException eee = new EarlyExitException(19, input);
					throw eee;
				}
				cnt19++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT"

	// $ANTLR start "SRC"
	public final void mSRC() throws RecognitionException {
		try {
			CommonToken file=null;
			CommonToken line=null;

			// org/antlr/v4/parse/ANTLRLexer.g:592:5: ( 'src' ( WSCHARS )+ file= ACTION_STRING_LITERAL ( WSCHARS )+ line= INT )
			// org/antlr/v4/parse/ANTLRLexer.g:592:7: 'src' ( WSCHARS )+ file= ACTION_STRING_LITERAL ( WSCHARS )+ line= INT
			{
			match("src"); if (state.failed) return;

			// org/antlr/v4/parse/ANTLRLexer.g:592:13: ( WSCHARS )+
			int cnt20=0;
			loop20:
			while (true) {
				int alt20=2;
				int LA20_0 = input.LA(1);
				if ( (LA20_0=='\t'||LA20_0=='\f'||LA20_0==' ') ) {
					alt20=1;
				}

				switch (alt20) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:
					{
					if ( input.LA(1)=='\t'||input.LA(1)=='\f'||input.LA(1)==' ' ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt20 >= 1 ) break loop20;
					if (state.backtracking>0) {state.failed=true; return;}
					EarlyExitException eee = new EarlyExitException(20, input);
					throw eee;
				}
				cnt20++;
			}

			int fileStart3345 = getCharIndex();
			int fileStartLine3345 = getLine();
			int fileStartCharPos3345 = getCharPositionInLine();
			mACTION_STRING_LITERAL(); if (state.failed) return;
			file = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, fileStart3345, getCharIndex()-1);
			file.setLine(fileStartLine3345);
			file.setCharPositionInLine(fileStartCharPos3345);

			// org/antlr/v4/parse/ANTLRLexer.g:592:49: ( WSCHARS )+
			int cnt21=0;
			loop21:
			while (true) {
				int alt21=2;
				int LA21_0 = input.LA(1);
				if ( (LA21_0=='\t'||LA21_0=='\f'||LA21_0==' ') ) {
					alt21=1;
				}

				switch (alt21) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:
					{
					if ( input.LA(1)=='\t'||input.LA(1)=='\f'||input.LA(1)==' ' ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt21 >= 1 ) break loop21;
					if (state.backtracking>0) {state.failed=true; return;}
					EarlyExitException eee = new EarlyExitException(21, input);
					throw eee;
				}
				cnt21++;
			}

			int lineStart3352 = getCharIndex();
			int lineStartLine3352 = getLine();
			int lineStartCharPos3352 = getCharPositionInLine();
			mINT(); if (state.failed) return;
			line = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, lineStart3352, getCharIndex()-1);
			line.setLine(lineStartLine3352);
			line.setCharPositionInLine(lineStartCharPos3352);

			if ( state.backtracking==0 ) {
			         // TODO: Add target specific code to change the source file name and current line number
			         //
			      }
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SRC"

	// $ANTLR start "STRING_LITERAL"
	public final void mSTRING_LITERAL() throws RecognitionException {
		try {
			int _type = STRING_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:607:5: ( '\\'' ( ( ESC_SEQ |~ ( '\\\\' | '\\'' | '\\r' | '\\n' ) ) )* ( '\\'' |) )
			// org/antlr/v4/parse/ANTLRLexer.g:607:8: '\\'' ( ( ESC_SEQ |~ ( '\\\\' | '\\'' | '\\r' | '\\n' ) ) )* ( '\\'' |)
			{
			match('\''); if (state.failed) return;
			// org/antlr/v4/parse/ANTLRLexer.g:607:13: ( ( ESC_SEQ |~ ( '\\\\' | '\\'' | '\\r' | '\\n' ) ) )*
			loop23:
			while (true) {
				int alt23=2;
				int LA23_0 = input.LA(1);
				if ( ((LA23_0 >= '\u0000' && LA23_0 <= '\t')||(LA23_0 >= '\u000B' && LA23_0 <= '\f')||(LA23_0 >= '\u000E' && LA23_0 <= '&')||(LA23_0 >= '(' && LA23_0 <= '\uFFFF')) ) {
					alt23=1;
				}

				switch (alt23) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:607:15: ( ESC_SEQ |~ ( '\\\\' | '\\'' | '\\r' | '\\n' ) )
					{
					// org/antlr/v4/parse/ANTLRLexer.g:607:15: ( ESC_SEQ |~ ( '\\\\' | '\\'' | '\\r' | '\\n' ) )
					int alt22=2;
					int LA22_0 = input.LA(1);
					if ( (LA22_0=='\\') ) {
						alt22=1;
					}
					else if ( ((LA22_0 >= '\u0000' && LA22_0 <= '\t')||(LA22_0 >= '\u000B' && LA22_0 <= '\f')||(LA22_0 >= '\u000E' && LA22_0 <= '&')||(LA22_0 >= '(' && LA22_0 <= '[')||(LA22_0 >= ']' && LA22_0 <= '\uFFFF')) ) {
						alt22=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return;}
						NoViableAltException nvae =
							new NoViableAltException("", 22, 0, input);
						throw nvae;
					}

					switch (alt22) {
						case 1 :
							// org/antlr/v4/parse/ANTLRLexer.g:607:17: ESC_SEQ
							{
							mESC_SEQ(); if (state.failed) return;

							}
							break;
						case 2 :
							// org/antlr/v4/parse/ANTLRLexer.g:607:27: ~ ( '\\\\' | '\\'' | '\\r' | '\\n' )
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
								state.failed=false;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return;}
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

					}

					}
					break;

				default :
					break loop23;
				}
			}

			// org/antlr/v4/parse/ANTLRLexer.g:608:8: ( '\\'' |)
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0=='\'') ) {
				alt24=1;
			}

			else {
				alt24=2;
			}

			switch (alt24) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:608:13: '\\''
					{
					match('\''); if (state.failed) return;
					}
					break;
				case 2 :
					// org/antlr/v4/parse/ANTLRLexer.g:610:13: 
					{
					if ( state.backtracking==0 ) {
					            Token t = new CommonToken(input, state.type, state.channel, state.tokenStartCharIndex, getCharIndex()-1);
					            t.setLine(state.tokenStartLine);
					            t.setText(state.text);
					            t.setCharPositionInLine(state.tokenStartCharPositionInLine);
					            grammarError(ErrorType.UNTERMINATED_STRING_LITERAL, t);
					            }
					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING_LITERAL"

	// $ANTLR start "HEX_DIGIT"
	public final void mHEX_DIGIT() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:623:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
			// org/antlr/v4/parse/ANTLRLexer.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
				input.consume();
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "HEX_DIGIT"

	// $ANTLR start "ESC_SEQ"
	public final void mESC_SEQ() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:630:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' | UNICODE_ESC | UNICODE_EXTENDED_ESC |~ ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' | 'u' ) ) )
			// org/antlr/v4/parse/ANTLRLexer.g:630:7: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' | UNICODE_ESC | UNICODE_EXTENDED_ESC |~ ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' | 'u' ) )
			{
			match('\\'); if (state.failed) return;
			// org/antlr/v4/parse/ANTLRLexer.g:631:9: ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' | UNICODE_ESC | UNICODE_EXTENDED_ESC |~ ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' | 'u' ) )
			int alt25=10;
			int LA25_0 = input.LA(1);
			if ( (LA25_0=='b') ) {
				alt25=1;
			}
			else if ( (LA25_0=='t') ) {
				alt25=2;
			}
			else if ( (LA25_0=='n') ) {
				alt25=3;
			}
			else if ( (LA25_0=='f') ) {
				alt25=4;
			}
			else if ( (LA25_0=='r') ) {
				alt25=5;
			}
			else if ( (LA25_0=='\'') ) {
				alt25=6;
			}
			else if ( (LA25_0=='\\') ) {
				alt25=7;
			}
			else if ( (LA25_0=='u') ) {
				int LA25_8 = input.LA(2);
				if ( (LA25_8=='{') ) {
					alt25=9;
				}

				else {
					alt25=8;
				}

			}
			else if ( ((LA25_0 >= '\u0000' && LA25_0 <= '&')||(LA25_0 >= '(' && LA25_0 <= '[')||(LA25_0 >= ']' && LA25_0 <= 'a')||(LA25_0 >= 'c' && LA25_0 <= 'e')||(LA25_0 >= 'g' && LA25_0 <= 'm')||(LA25_0 >= 'o' && LA25_0 <= 'q')||LA25_0=='s'||(LA25_0 >= 'v' && LA25_0 <= '\uFFFF')) ) {
				alt25=10;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return;}
				NoViableAltException nvae =
					new NoViableAltException("", 25, 0, input);
				throw nvae;
			}

			switch (alt25) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:633:9: 'b'
					{
					match('b'); if (state.failed) return;
					}
					break;
				case 2 :
					// org/antlr/v4/parse/ANTLRLexer.g:633:13: 't'
					{
					match('t'); if (state.failed) return;
					}
					break;
				case 3 :
					// org/antlr/v4/parse/ANTLRLexer.g:633:17: 'n'
					{
					match('n'); if (state.failed) return;
					}
					break;
				case 4 :
					// org/antlr/v4/parse/ANTLRLexer.g:633:21: 'f'
					{
					match('f'); if (state.failed) return;
					}
					break;
				case 5 :
					// org/antlr/v4/parse/ANTLRLexer.g:633:25: 'r'
					{
					match('r'); if (state.failed) return;
					}
					break;
				case 6 :
					// org/antlr/v4/parse/ANTLRLexer.g:633:29: '\\''
					{
					match('\''); if (state.failed) return;
					}
					break;
				case 7 :
					// org/antlr/v4/parse/ANTLRLexer.g:633:34: '\\\\'
					{
					match('\\'); if (state.failed) return;
					}
					break;
				case 8 :
					// org/antlr/v4/parse/ANTLRLexer.g:636:12: UNICODE_ESC
					{
					mUNICODE_ESC(); if (state.failed) return;

					}
					break;
				case 9 :
					// org/antlr/v4/parse/ANTLRLexer.g:639:15: UNICODE_EXTENDED_ESC
					{
					mUNICODE_EXTENDED_ESC(); if (state.failed) return;

					}
					break;
				case 10 :
					// org/antlr/v4/parse/ANTLRLexer.g:642:12: ~ ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' | 'u' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= 'a')||(input.LA(1) >= 'c' && input.LA(1) <= 'e')||(input.LA(1) >= 'g' && input.LA(1) <= 'm')||(input.LA(1) >= 'o' && input.LA(1) <= 'q')||input.LA(1)=='s'||(input.LA(1) >= 'v' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( state.backtracking==0 ) {
					                Token t = new CommonToken(input, state.type, state.channel, getCharIndex()-2, getCharIndex()-1);
					                t.setText(t.getText());
					                t.setLine(input.getLine());
					                t.setCharPositionInLine(input.getCharPositionInLine()-2);
					                grammarError(ErrorType.INVALID_ESCAPE_SEQUENCE, t, input.substring(getCharIndex()-2,getCharIndex()-1));
					    	      }
					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ESC_SEQ"

	// $ANTLR start "UNICODE_ESC"
	public final void mUNICODE_ESC() throws RecognitionException {
		try {


				// Flag to tell us whether we have a valid number of
				// hex digits in the escape sequence
				//
				int	hCount = 0;

			// org/antlr/v4/parse/ANTLRLexer.g:662:5: ( 'u' ( ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT |) |) |) ) |) )
			// org/antlr/v4/parse/ANTLRLexer.g:662:9: 'u' ( ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT |) |) |) ) |)
			{
			match('u'); if (state.failed) return;
			// org/antlr/v4/parse/ANTLRLexer.g:671:6: ( ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT |) |) |) ) |)
			int alt29=2;
			int LA29_0 = input.LA(1);
			if ( ((LA29_0 >= '0' && LA29_0 <= '9')||(LA29_0 >= 'A' && LA29_0 <= 'F')||(LA29_0 >= 'a' && LA29_0 <= 'f')) ) {
				alt29=1;
			}

			else {
				alt29=2;
			}

			switch (alt29) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:672:9: ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT |) |) |) )
					{
					// org/antlr/v4/parse/ANTLRLexer.g:672:9: ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT |) |) |) )
					// org/antlr/v4/parse/ANTLRLexer.g:673:12: HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT |) |) |)
					{
					mHEX_DIGIT(); if (state.failed) return;

					if ( state.backtracking==0 ) { hCount++; }
					// org/antlr/v4/parse/ANTLRLexer.g:674:14: ( HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT |) |) |)
					int alt28=2;
					int LA28_0 = input.LA(1);
					if ( ((LA28_0 >= '0' && LA28_0 <= '9')||(LA28_0 >= 'A' && LA28_0 <= 'F')||(LA28_0 >= 'a' && LA28_0 <= 'f')) ) {
						alt28=1;
					}

					else {
						alt28=2;
					}

					switch (alt28) {
						case 1 :
							// org/antlr/v4/parse/ANTLRLexer.g:675:19: HEX_DIGIT ( HEX_DIGIT ( HEX_DIGIT |) |)
							{
							mHEX_DIGIT(); if (state.failed) return;

							if ( state.backtracking==0 ) { hCount++; }
							// org/antlr/v4/parse/ANTLRLexer.g:676:16: ( HEX_DIGIT ( HEX_DIGIT |) |)
							int alt27=2;
							int LA27_0 = input.LA(1);
							if ( ((LA27_0 >= '0' && LA27_0 <= '9')||(LA27_0 >= 'A' && LA27_0 <= 'F')||(LA27_0 >= 'a' && LA27_0 <= 'f')) ) {
								alt27=1;
							}

							else {
								alt27=2;
							}

							switch (alt27) {
								case 1 :
									// org/antlr/v4/parse/ANTLRLexer.g:677:21: HEX_DIGIT ( HEX_DIGIT |)
									{
									mHEX_DIGIT(); if (state.failed) return;

									if ( state.backtracking==0 ) { hCount++; }
									// org/antlr/v4/parse/ANTLRLexer.g:678:21: ( HEX_DIGIT |)
									int alt26=2;
									int LA26_0 = input.LA(1);
									if ( ((LA26_0 >= '0' && LA26_0 <= '9')||(LA26_0 >= 'A' && LA26_0 <= 'F')||(LA26_0 >= 'a' && LA26_0 <= 'f')) ) {
										alt26=1;
									}

									else {
										alt26=2;
									}

									switch (alt26) {
										case 1 :
											// org/antlr/v4/parse/ANTLRLexer.g:681:25: HEX_DIGIT
											{
											mHEX_DIGIT(); if (state.failed) return;

											if ( state.backtracking==0 ) { hCount++; }
											}
											break;
										case 2 :
											// org/antlr/v4/parse/ANTLRLexer.g:684:21: 
											{
											}
											break;

									}

									}
									break;
								case 2 :
									// org/antlr/v4/parse/ANTLRLexer.g:687:17: 
									{
									}
									break;

							}

							}
							break;
						case 2 :
							// org/antlr/v4/parse/ANTLRLexer.g:690:11: 
							{
							}
							break;

					}

					}

					}
					break;
				case 2 :
					// org/antlr/v4/parse/ANTLRLexer.g:693:6: 
					{
					}
					break;

			}

			if ( state.backtracking==0 ) {
			    		if (hCount < 4) {
							Interval badRange = Interval.of(getCharIndex()-2-hCount, getCharIndex());
							String lastChar = input.substring(badRange.b, badRange.b);
							if ( lastChar.codePointAt(0)=='\'' ) {
								badRange.b--;
							}
							String bad = input.substring(badRange.a, badRange.b);
							Token t = new CommonToken(input, state.type, state.channel, badRange.a, badRange.b);
							t.setLine(input.getLine());
							t.setCharPositionInLine(input.getCharPositionInLine()-hCount-2);
							grammarError(ErrorType.INVALID_ESCAPE_SEQUENCE, t, bad);
			    		}
			    	}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "UNICODE_ESC"

	// $ANTLR start "UNICODE_EXTENDED_ESC"
	public final void mUNICODE_EXTENDED_ESC() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:715:5: ( 'u{' ( HEX_DIGIT )+ '}' )
			// org/antlr/v4/parse/ANTLRLexer.g:715:9: 'u{' ( HEX_DIGIT )+ '}'
			{
			match("u{"); if (state.failed) return;

			// org/antlr/v4/parse/ANTLRLexer.g:717:9: ( HEX_DIGIT )+
			int cnt30=0;
			loop30:
			while (true) {
				int alt30=2;
				int LA30_0 = input.LA(1);
				if ( ((LA30_0 >= '0' && LA30_0 <= '9')||(LA30_0 >= 'A' && LA30_0 <= 'F')||(LA30_0 >= 'a' && LA30_0 <= 'f')) ) {
					alt30=1;
				}

				switch (alt30) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt30 >= 1 ) break loop30;
					if (state.backtracking>0) {state.failed=true; return;}
					EarlyExitException eee = new EarlyExitException(30, input);
					throw eee;
				}
				cnt30++;
			}

			match('}'); if (state.failed) return;
			if ( state.backtracking==0 ) {
			            int numDigits = getCharIndex()-state.tokenStartCharIndex-6;
			            if (numDigits > 6) {
			                Token t = new CommonToken(input, state.type, state.channel, state.tokenStartCharIndex, getCharIndex()-1);
			                t.setText(t.getText());
			                t.setLine(input.getLine());
			                t.setCharPositionInLine(input.getCharPositionInLine()-numDigits);
			                grammarError(ErrorType.INVALID_ESCAPE_SEQUENCE, t, input.substring(state.tokenStartCharIndex,getCharIndex()-1));
						}
			        }
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "UNICODE_EXTENDED_ESC"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:742:5: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\f' )+ )
			// org/antlr/v4/parse/ANTLRLexer.g:742:7: ( ' ' | '\\t' | '\\r' | '\\n' | '\\f' )+
			{
			// org/antlr/v4/parse/ANTLRLexer.g:742:7: ( ' ' | '\\t' | '\\r' | '\\n' | '\\f' )+
			int cnt31=0;
			loop31:
			while (true) {
				int alt31=2;
				int LA31_0 = input.LA(1);
				if ( ((LA31_0 >= '\t' && LA31_0 <= '\n')||(LA31_0 >= '\f' && LA31_0 <= '\r')||LA31_0==' ') ) {
					alt31=1;
				}

				switch (alt31) {
				case 1 :
					// org/antlr/v4/parse/ANTLRLexer.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt31 >= 1 ) break loop31;
					if (state.backtracking>0) {state.failed=true; return;}
					EarlyExitException eee = new EarlyExitException(31, input);
					throw eee;
				}
				cnt31++;
			}

			if ( state.backtracking==0 ) {_channel=HIDDEN;}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	// $ANTLR start "NLCHARS"
	public final void mNLCHARS() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:757:5: ( '\\n' | '\\r' )
			// org/antlr/v4/parse/ANTLRLexer.g:
			{
			if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
				input.consume();
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NLCHARS"

	// $ANTLR start "WSCHARS"
	public final void mWSCHARS() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:765:5: ( ' ' | '\\t' | '\\f' )
			// org/antlr/v4/parse/ANTLRLexer.g:
			{
			if ( input.LA(1)=='\t'||input.LA(1)=='\f'||input.LA(1)==' ' ) {
				input.consume();
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WSCHARS"

	// $ANTLR start "WSNLCHARS"
	public final void mWSNLCHARS() throws RecognitionException {
		try {
			// org/antlr/v4/parse/ANTLRLexer.g:774:5: ( ' ' | '\\t' | '\\f' | '\\n' | '\\r' )
			// org/antlr/v4/parse/ANTLRLexer.g:
			{
			if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
				input.consume();
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WSNLCHARS"

	// $ANTLR start "UnicodeBOM"
	public final void mUnicodeBOM() throws RecognitionException {
		try {
			int _type = UnicodeBOM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:783:5: ( '\\uFEFF' )
			// org/antlr/v4/parse/ANTLRLexer.g:783:9: '\\uFEFF'
			{
			match('\uFEFF'); if (state.failed) return;
			if ( state.backtracking==0 ) {skip();}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "UnicodeBOM"

	// $ANTLR start "ERRCHAR"
	public final void mERRCHAR() throws RecognitionException {
		try {
			int _type = ERRCHAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// org/antlr/v4/parse/ANTLRLexer.g:798:5: ( . )
			// org/antlr/v4/parse/ANTLRLexer.g:798:7: .
			{
			matchAny(); if (state.failed) return;
			if ( state.backtracking==0 ) {
			         Token t = new CommonToken(input, state.type, state.channel, state.tokenStartCharIndex, getCharIndex()-1);
			         t.setLine(state.tokenStartLine);
			         t.setText(state.text);
			         t.setCharPositionInLine(state.tokenStartCharPositionInLine);
			         String msg = getTokenErrorDisplay(t) + " came as a complete surprise to me";
			         grammarError(ErrorType.SYNTAX_ERROR, t, msg);
			         state.syntaxErrors++;
			         skip();
			      }
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ERRCHAR"

	@Override
	public void mTokens() throws RecognitionException {
		// org/antlr/v4/parse/ANTLRLexer.g:1:8: ( COMMENT | ARG_OR_CHARSET | ACTION | OPTIONS | TOKENS_SPEC | CHANNELS | IMPORT | FRAGMENT | LEXER | PARSER | GRAMMAR | RETURNS | LOCALS | THROWS | CATCH | FINALLY | MODE | COLON | COLONCOLON | COMMA | SEMI | LPAREN | RPAREN | RARROW | LT | GT | ASSIGN | QUESTION | STAR | PLUS | PLUS_ASSIGN | OR | DOLLAR | DOT | RANGE | AT | POUND | NOT | RBRACE | ID | INT | STRING_LITERAL | WS | UnicodeBOM | ERRCHAR )
		int alt32=45;
		alt32 = dfa32.predict(input);
		switch (alt32) {
			case 1 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:10: COMMENT
				{
				mCOMMENT(); if (state.failed) return;

				}
				break;
			case 2 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:18: ARG_OR_CHARSET
				{
				mARG_OR_CHARSET(); if (state.failed) return;

				}
				break;
			case 3 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:33: ACTION
				{
				mACTION(); if (state.failed) return;

				}
				break;
			case 4 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:40: OPTIONS
				{
				mOPTIONS(); if (state.failed) return;

				}
				break;
			case 5 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:48: TOKENS_SPEC
				{
				mTOKENS_SPEC(); if (state.failed) return;

				}
				break;
			case 6 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:60: CHANNELS
				{
				mCHANNELS(); if (state.failed) return;

				}
				break;
			case 7 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:69: IMPORT
				{
				mIMPORT(); if (state.failed) return;

				}
				break;
			case 8 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:76: FRAGMENT
				{
				mFRAGMENT(); if (state.failed) return;

				}
				break;
			case 9 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:85: LEXER
				{
				mLEXER(); if (state.failed) return;

				}
				break;
			case 10 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:91: PARSER
				{
				mPARSER(); if (state.failed) return;

				}
				break;
			case 11 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:98: GRAMMAR
				{
				mGRAMMAR(); if (state.failed) return;

				}
				break;
			case 12 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:106: RETURNS
				{
				mRETURNS(); if (state.failed) return;

				}
				break;
			case 13 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:114: LOCALS
				{
				mLOCALS(); if (state.failed) return;

				}
				break;
			case 14 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:121: THROWS
				{
				mTHROWS(); if (state.failed) return;

				}
				break;
			case 15 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:128: CATCH
				{
				mCATCH(); if (state.failed) return;

				}
				break;
			case 16 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:134: FINALLY
				{
				mFINALLY(); if (state.failed) return;

				}
				break;
			case 17 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:142: MODE
				{
				mMODE(); if (state.failed) return;

				}
				break;
			case 18 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:147: COLON
				{
				mCOLON(); if (state.failed) return;

				}
				break;
			case 19 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:153: COLONCOLON
				{
				mCOLONCOLON(); if (state.failed) return;

				}
				break;
			case 20 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:164: COMMA
				{
				mCOMMA(); if (state.failed) return;

				}
				break;
			case 21 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:170: SEMI
				{
				mSEMI(); if (state.failed) return;

				}
				break;
			case 22 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:175: LPAREN
				{
				mLPAREN(); if (state.failed) return;

				}
				break;
			case 23 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:182: RPAREN
				{
				mRPAREN(); if (state.failed) return;

				}
				break;
			case 24 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:189: RARROW
				{
				mRARROW(); if (state.failed) return;

				}
				break;
			case 25 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:196: LT
				{
				mLT(); if (state.failed) return;

				}
				break;
			case 26 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:199: GT
				{
				mGT(); if (state.failed) return;

				}
				break;
			case 27 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:202: ASSIGN
				{
				mASSIGN(); if (state.failed) return;

				}
				break;
			case 28 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:209: QUESTION
				{
				mQUESTION(); if (state.failed) return;

				}
				break;
			case 29 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:218: STAR
				{
				mSTAR(); if (state.failed) return;

				}
				break;
			case 30 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:223: PLUS
				{
				mPLUS(); if (state.failed) return;

				}
				break;
			case 31 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:228: PLUS_ASSIGN
				{
				mPLUS_ASSIGN(); if (state.failed) return;

				}
				break;
			case 32 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:240: OR
				{
				mOR(); if (state.failed) return;

				}
				break;
			case 33 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:243: DOLLAR
				{
				mDOLLAR(); if (state.failed) return;

				}
				break;
			case 34 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:250: DOT
				{
				mDOT(); if (state.failed) return;

				}
				break;
			case 35 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:254: RANGE
				{
				mRANGE(); if (state.failed) return;

				}
				break;
			case 36 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:260: AT
				{
				mAT(); if (state.failed) return;

				}
				break;
			case 37 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:263: POUND
				{
				mPOUND(); if (state.failed) return;

				}
				break;
			case 38 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:269: NOT
				{
				mNOT(); if (state.failed) return;

				}
				break;
			case 39 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:273: RBRACE
				{
				mRBRACE(); if (state.failed) return;

				}
				break;
			case 40 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:280: ID
				{
				mID(); if (state.failed) return;

				}
				break;
			case 41 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:283: INT
				{
				mINT(); if (state.failed) return;

				}
				break;
			case 42 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:287: STRING_LITERAL
				{
				mSTRING_LITERAL(); if (state.failed) return;

				}
				break;
			case 43 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:302: WS
				{
				mWS(); if (state.failed) return;

				}
				break;
			case 44 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:305: UnicodeBOM
				{
				mUnicodeBOM(); if (state.failed) return;

				}
				break;
			case 45 :
				// org/antlr/v4/parse/ANTLRLexer.g:1:316: ERRCHAR
				{
				mERRCHAR(); if (state.failed) return;

				}
				break;

		}
	}

	// $ANTLR start synpred1_ANTLRLexer
	public final void synpred1_ANTLRLexer_fragment() throws RecognitionException {
		// org/antlr/v4/parse/ANTLRLexer.g:229:17: ( ' $ANTLR' )
		// org/antlr/v4/parse/ANTLRLexer.g:229:18: ' $ANTLR'
		{
		match(" $ANTLR"); if (state.failed) return;

		}

	}
	// $ANTLR end synpred1_ANTLRLexer

	// $ANTLR start synpred2_ANTLRLexer
	public final void synpred2_ANTLRLexer_fragment() throws RecognitionException {
		// org/antlr/v4/parse/ANTLRLexer.g:323:14: ( '\"' )
		// org/antlr/v4/parse/ANTLRLexer.g:323:15: '\"'
		{
		match('\"'); if (state.failed) return;
		}

	}
	// $ANTLR end synpred2_ANTLRLexer

	// $ANTLR start synpred3_ANTLRLexer
	public final void synpred3_ANTLRLexer_fragment() throws RecognitionException {
		// org/antlr/v4/parse/ANTLRLexer.g:325:14: ( '\\'' )
		// org/antlr/v4/parse/ANTLRLexer.g:325:15: '\\''
		{
		match('\''); if (state.failed) return;
		}

	}
	// $ANTLR end synpred3_ANTLRLexer

	// $ANTLR start synpred4_ANTLRLexer
	public final void synpred4_ANTLRLexer_fragment() throws RecognitionException {
		// org/antlr/v4/parse/ANTLRLexer.g:552:10: ( '\\\\' )
		// org/antlr/v4/parse/ANTLRLexer.g:552:11: '\\\\'
		{
		match('\\'); if (state.failed) return;
		}

	}
	// $ANTLR end synpred4_ANTLRLexer

	// $ANTLR start synpred5_ANTLRLexer
	public final void synpred5_ANTLRLexer_fragment() throws RecognitionException {
		// org/antlr/v4/parse/ANTLRLexer.g:562:9: ( '\\\\' )
		// org/antlr/v4/parse/ANTLRLexer.g:562:10: '\\\\'
		{
		match('\\'); if (state.failed) return;
		}

	}
	// $ANTLR end synpred5_ANTLRLexer

	public final boolean synpred1_ANTLRLexer() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred1_ANTLRLexer_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred2_ANTLRLexer() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred2_ANTLRLexer_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred5_ANTLRLexer() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred5_ANTLRLexer_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred3_ANTLRLexer() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred3_ANTLRLexer_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred4_ANTLRLexer() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred4_ANTLRLexer_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}


	protected DFA2 dfa2 = new DFA2(this);
	protected DFA32 dfa32 = new DFA32(this);
	static final String DFA2_eotS =
		"\2\2\1\uffff\16\2\1\uffff\3\2\1\uffff\2\2\2\uffff";
	static final String DFA2_eofS =
		"\32\uffff";
	static final String DFA2_minS =
		"\1\40\1\44\1\uffff\1\101\1\116\1\124\1\114\1\122\1\163\1\162\1\143\2\11"+
		"\3\0\1\11\1\uffff\3\0\1\uffff\1\11\3\0";
	static final String DFA2_maxS =
		"\1\40\1\44\1\uffff\1\101\1\116\1\124\1\114\1\122\1\163\1\162\1\143\1\40"+
		"\1\42\3\uffff\1\40\1\uffff\3\uffff\1\uffff\1\71\1\uffff\2\0";
	static final String DFA2_acceptS =
		"\2\uffff\1\2\16\uffff\1\1\3\uffff\1\1\4\uffff";
	static final String DFA2_specialS =
		"\15\uffff\1\2\1\4\1\5\2\uffff\1\3\1\7\1\10\2\uffff\1\6\1\1\1\0}>";
	static final String[] DFA2_transitionS = {
			"\1\1",
			"\1\3",
			"",
			"\1\4",
			"\1\5",
			"\1\6",
			"\1\7",
			"\1\10",
			"\1\11",
			"\1\12",
			"\1\13",
			"\1\14\2\uffff\1\14\23\uffff\1\14",
			"\1\14\2\uffff\1\14\23\uffff\1\14\1\uffff\1\15",
			"\12\17\1\21\2\17\1\21\24\17\1\20\71\17\1\16\uffa3\17",
			"\12\24\1\25\2\24\1\25\24\24\1\22\71\24\1\23\uffa3\24",
			"\12\17\1\21\2\17\1\21\24\17\1\20\71\17\1\16\uffa3\17",
			"\1\26\2\uffff\1\26\23\uffff\1\26",
			"",
			"\11\17\1\27\1\21\1\17\1\27\1\21\22\17\1\27\1\17\1\20\71\17\1\16\uffa3"+
			"\17",
			"\12\24\1\25\2\24\1\25\24\24\1\22\71\24\1\23\uffa3\24",
			"\12\17\1\21\2\17\1\21\24\17\1\20\71\17\1\16\uffa3\17",
			"",
			"\1\26\2\uffff\1\26\23\uffff\1\26\17\uffff\12\30",
			"\11\17\1\27\1\21\1\17\1\27\1\21\22\17\1\27\1\17\1\20\15\17\12\31\42"+
			"\17\1\16\uffa3\17",
			"\1\uffff",
			"\1\uffff"
	};

	static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
	static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
	static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
	static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
	static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
	static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
	static final short[][] DFA2_transition;

	static {
		int numStates = DFA2_transitionS.length;
		DFA2_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
		}
	}

	protected class DFA2 extends DFA {

		public DFA2(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 2;
			this.eot = DFA2_eot;
			this.eof = DFA2_eof;
			this.min = DFA2_min;
			this.max = DFA2_max;
			this.accept = DFA2_accept;
			this.special = DFA2_special;
			this.transition = DFA2_transition;
		}
		@Override
		public String getDescription() {
			return "228:13: ( ( ' $ANTLR' )=> ' $ANTLR' SRC | (~ NLCHARS )* )";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA2_25 = input.LA(1);
						 
						int index2_25 = input.index();
						input.rewind();
						s = -1;
						if ( (synpred1_ANTLRLexer()) ) {s = 21;}
						else if ( (true) ) {s = 2;}
						 
						input.seek(index2_25);
						if ( s>=0 ) return s;
						break;
					case 1 : 
						int LA2_24 = input.LA(1);
						 
						int index2_24 = input.index();
						input.rewind();
						s = -1;
						if ( (synpred1_ANTLRLexer()) ) {s = 21;}
						else if ( (true) ) {s = 2;}
						 
						input.seek(index2_24);
						if ( s>=0 ) return s;
						break;
					case 2 : 
						int LA2_13 = input.LA(1);
						 
						int index2_13 = input.index();
						input.rewind();
						s = -1;
						if ( (LA2_13=='\\') ) {s = 14;}
						else if ( ((LA2_13 >= '\u0000' && LA2_13 <= '\t')||(LA2_13 >= '\u000B' && LA2_13 <= '\f')||(LA2_13 >= '\u000E' && LA2_13 <= '!')||(LA2_13 >= '#' && LA2_13 <= '[')||(LA2_13 >= ']' && LA2_13 <= '\uFFFF')) ) {s = 15;}
						else if ( (LA2_13=='\"') ) {s = 16;}
						else if ( (LA2_13=='\n'||LA2_13=='\r') && (synpred1_ANTLRLexer())) {s = 17;}
						else s = 2;
						 
						input.seek(index2_13);
						if ( s>=0 ) return s;
						break;
					case 3 : 
						int LA2_18 = input.LA(1);
						 
						int index2_18 = input.index();
						input.rewind();
						s = -1;
						if ( (LA2_18=='\"') ) {s = 16;}
						else if ( (LA2_18=='\\') ) {s = 14;}
						else if ( (LA2_18=='\t'||LA2_18=='\f'||LA2_18==' ') ) {s = 23;}
						else if ( ((LA2_18 >= '\u0000' && LA2_18 <= '\b')||LA2_18=='\u000B'||(LA2_18 >= '\u000E' && LA2_18 <= '\u001F')||LA2_18=='!'||(LA2_18 >= '#' && LA2_18 <= '[')||(LA2_18 >= ']' && LA2_18 <= '\uFFFF')) ) {s = 15;}
						else if ( (LA2_18=='\n'||LA2_18=='\r') && (synpred1_ANTLRLexer())) {s = 17;}
						else s = 2;
						 
						input.seek(index2_18);
						if ( s>=0 ) return s;
						break;
					case 4 : 
						int LA2_14 = input.LA(1);
						 
						int index2_14 = input.index();
						input.rewind();
						s = -1;
						if ( (LA2_14=='\"') ) {s = 18;}
						else if ( (LA2_14=='\\') ) {s = 19;}
						else if ( ((LA2_14 >= '\u0000' && LA2_14 <= '\t')||(LA2_14 >= '\u000B' && LA2_14 <= '\f')||(LA2_14 >= '\u000E' && LA2_14 <= '!')||(LA2_14 >= '#' && LA2_14 <= '[')||(LA2_14 >= ']' && LA2_14 <= '\uFFFF')) ) {s = 20;}
						else if ( (LA2_14=='\n'||LA2_14=='\r') && (synpred1_ANTLRLexer())) {s = 21;}
						else s = 2;
						 
						input.seek(index2_14);
						if ( s>=0 ) return s;
						break;
					case 5 : 
						int LA2_15 = input.LA(1);
						 
						int index2_15 = input.index();
						input.rewind();
						s = -1;
						if ( (LA2_15=='\"') ) {s = 16;}
						else if ( (LA2_15=='\\') ) {s = 14;}
						else if ( ((LA2_15 >= '\u0000' && LA2_15 <= '\t')||(LA2_15 >= '\u000B' && LA2_15 <= '\f')||(LA2_15 >= '\u000E' && LA2_15 <= '!')||(LA2_15 >= '#' && LA2_15 <= '[')||(LA2_15 >= ']' && LA2_15 <= '\uFFFF')) ) {s = 15;}
						else if ( (LA2_15=='\n'||LA2_15=='\r') && (synpred1_ANTLRLexer())) {s = 17;}
						else s = 2;
						 
						input.seek(index2_15);
						if ( s>=0 ) return s;
						break;
					case 6 : 
						int LA2_23 = input.LA(1);
						 
						int index2_23 = input.index();
						input.rewind();
						s = -1;
						if ( (LA2_23=='\"') ) {s = 16;}
						else if ( (LA2_23=='\\') ) {s = 14;}
						else if ( ((LA2_23 >= '0' && LA2_23 <= '9')) ) {s = 25;}
						else if ( (LA2_23=='\t'||LA2_23=='\f'||LA2_23==' ') ) {s = 23;}
						else if ( ((LA2_23 >= '\u0000' && LA2_23 <= '\b')||LA2_23=='\u000B'||(LA2_23 >= '\u000E' && LA2_23 <= '\u001F')||LA2_23=='!'||(LA2_23 >= '#' && LA2_23 <= '/')||(LA2_23 >= ':' && LA2_23 <= '[')||(LA2_23 >= ']' && LA2_23 <= '\uFFFF')) ) {s = 15;}
						else if ( (LA2_23=='\n'||LA2_23=='\r') && (synpred1_ANTLRLexer())) {s = 17;}
						else s = 2;
						 
						input.seek(index2_23);
						if ( s>=0 ) return s;
						break;
					case 7 : 
						int LA2_19 = input.LA(1);
						 
						int index2_19 = input.index();
						input.rewind();
						s = -1;
						if ( (LA2_19=='\"') ) {s = 18;}
						else if ( (LA2_19=='\\') ) {s = 19;}
						else if ( ((LA2_19 >= '\u0000' && LA2_19 <= '\t')||(LA2_19 >= '\u000B' && LA2_19 <= '\f')||(LA2_19 >= '\u000E' && LA2_19 <= '!')||(LA2_19 >= '#' && LA2_19 <= '[')||(LA2_19 >= ']' && LA2_19 <= '\uFFFF')) ) {s = 20;}
						else if ( (LA2_19=='\n'||LA2_19=='\r') && (synpred1_ANTLRLexer())) {s = 21;}
						else s = 2;
						 
						input.seek(index2_19);
						if ( s>=0 ) return s;
						break;
					case 8 : 
						int LA2_20 = input.LA(1);
						 
						int index2_20 = input.index();
						input.rewind();
						s = -1;
						if ( (LA2_20=='\"') ) {s = 16;}
						else if ( (LA2_20=='\\') ) {s = 14;}
						else if ( ((LA2_20 >= '\u0000' && LA2_20 <= '\t')||(LA2_20 >= '\u000B' && LA2_20 <= '\f')||(LA2_20 >= '\u000E' && LA2_20 <= '!')||(LA2_20 >= '#' && LA2_20 <= '[')||(LA2_20 >= ']' && LA2_20 <= '\uFFFF')) ) {s = 15;}
						else if ( (LA2_20=='\n'||LA2_20=='\r') && (synpred1_ANTLRLexer())) {s = 17;}
						else s = 2;
						 
						input.seek(index2_20);
						if ( s>=0 ) return s;
						break;
			}
			if (state.backtracking>0) {state.failed=true; return -1;}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 2, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	static final String DFA32_eotS =
		"\2\uffff\1\46\1\uffff\12\54\1\73\4\uffff\1\46\5\uffff\1\107\2\uffff\1"+
		"\113\16\uffff\1\54\1\uffff\15\54\32\uffff\33\54\1\175\4\54\1\u0082\3\54"+
		"\1\u0086\4\54\1\uffff\2\54\1\u008d\1\54\1\uffff\1\u008f\2\54\1\uffff\1"+
		"\u0092\1\u0093\3\54\2\uffff\1\54\1\uffff\1\54\1\u0099\2\uffff\1\u009a"+
		"\1\u009b\1\uffff\1\54\1\u009d\5\uffff";
	static final String DFA32_eofS =
		"\u009e\uffff";
	static final String DFA32_minS =
		"\1\0\1\uffff\1\0\1\uffff\1\160\1\150\1\141\1\155\1\151\1\145\1\141\1\162"+
		"\1\145\1\157\1\72\4\uffff\1\76\5\uffff\1\75\2\uffff\1\56\16\uffff\1\164"+
		"\1\uffff\1\153\1\162\1\141\1\164\1\160\1\141\1\156\1\170\1\143\1\162\1"+
		"\141\1\164\1\144\32\uffff\1\151\1\145\1\157\1\156\1\143\1\157\1\147\1"+
		"\141\1\145\1\141\1\163\1\155\1\165\1\145\1\157\1\156\1\167\1\156\1\150"+
		"\1\162\1\155\1\154\1\162\1\154\1\145\1\155\1\162\1\60\1\156\2\163\1\145"+
		"\1\60\1\164\1\145\1\154\1\60\1\163\1\162\1\141\1\156\1\uffff\1\163\1\11"+
		"\1\60\1\154\1\uffff\1\60\1\156\1\171\1\uffff\2\60\1\162\1\163\1\11\2\uffff"+
		"\1\163\1\uffff\1\164\1\60\2\uffff\2\60\1\uffff\1\11\1\60\5\uffff";
	static final String DFA32_maxS =
		"\1\uffff\1\uffff\1\uffff\1\uffff\1\160\1\157\1\150\1\155\1\162\1\157\1"+
		"\141\1\162\1\145\1\157\1\72\4\uffff\1\76\5\uffff\1\75\2\uffff\1\56\16"+
		"\uffff\1\164\1\uffff\1\153\1\162\1\141\1\164\1\160\1\141\1\156\1\170\1"+
		"\143\1\162\1\141\1\164\1\144\32\uffff\1\151\1\145\1\157\1\156\1\143\1"+
		"\157\1\147\1\141\1\145\1\141\1\163\1\155\1\165\1\145\1\157\1\156\1\167"+
		"\1\156\1\150\1\162\1\155\1\154\1\162\1\154\1\145\1\155\1\162\1\ufffd\1"+
		"\156\2\163\1\145\1\ufffd\1\164\1\145\1\154\1\ufffd\1\163\1\162\1\141\1"+
		"\156\1\uffff\1\163\1\173\1\ufffd\1\154\1\uffff\1\ufffd\1\156\1\171\1\uffff"+
		"\2\ufffd\1\162\1\163\1\173\2\uffff\1\163\1\uffff\1\164\1\ufffd\2\uffff"+
		"\2\ufffd\1\uffff\1\173\1\ufffd\5\uffff";
	static final String DFA32_acceptS =
		"\1\uffff\1\1\1\uffff\1\3\13\uffff\1\24\1\25\1\26\1\27\1\uffff\1\31\1\32"+
		"\1\33\1\34\1\35\1\uffff\1\40\1\41\1\uffff\1\44\1\45\1\46\1\47\1\50\1\51"+
		"\1\52\1\53\1\54\1\55\1\1\2\2\1\3\1\uffff\1\50\15\uffff\1\23\1\22\1\24"+
		"\1\25\1\26\1\27\1\30\1\31\1\32\1\33\1\34\1\35\1\37\1\36\1\40\1\41\1\43"+
		"\1\42\1\44\1\45\1\46\1\47\1\51\1\52\1\53\1\54\51\uffff\1\21\4\uffff\1"+
		"\17\3\uffff\1\11\5\uffff\1\5\1\16\1\uffff\1\7\2\uffff\1\15\1\12\2\uffff"+
		"\1\4\2\uffff\1\20\1\13\1\14\1\6\1\10";
	static final String DFA32_specialS =
		"\1\0\1\uffff\1\1\u009b\uffff}>";
	static final String[] DFA32_transitionS = {
			"\11\46\2\44\1\46\2\44\22\46\1\44\2\46\1\36\1\33\2\46\1\43\1\21\1\22\1"+
			"\30\1\31\1\17\1\23\1\34\1\1\12\42\1\16\1\20\1\24\1\26\1\25\1\27\1\35"+
			"\32\41\1\2\5\46\2\41\1\6\2\41\1\10\1\13\1\41\1\7\2\41\1\11\1\15\1\41"+
			"\1\4\1\12\1\41\1\14\1\41\1\5\6\41\1\3\1\32\1\40\1\37\101\46\27\41\1\46"+
			"\37\41\1\46\u0208\41\160\46\16\41\1\46\u1c81\41\14\46\2\41\142\46\u0120"+
			"\41\u0a70\46\u03f0\41\21\46\ua7ff\41\u2100\46\u04d0\41\40\46\u010f\41"+
			"\1\45\u00fe\41\2\46",
			"",
			"\12\50\1\51\2\50\1\51\ufff2\50",
			"",
			"\1\53",
			"\1\56\6\uffff\1\55",
			"\1\60\6\uffff\1\57",
			"\1\61",
			"\1\63\10\uffff\1\62",
			"\1\64\11\uffff\1\65",
			"\1\66",
			"\1\67",
			"\1\70",
			"\1\71",
			"\1\72",
			"",
			"",
			"",
			"",
			"\1\100",
			"",
			"",
			"",
			"",
			"",
			"\1\106",
			"",
			"",
			"\1\112",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\124",
			"",
			"\1\125",
			"\1\126",
			"\1\127",
			"\1\130",
			"\1\131",
			"\1\132",
			"\1\133",
			"\1\134",
			"\1\135",
			"\1\136",
			"\1\137",
			"\1\140",
			"\1\141",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\142",
			"\1\143",
			"\1\144",
			"\1\145",
			"\1\146",
			"\1\147",
			"\1\150",
			"\1\151",
			"\1\152",
			"\1\153",
			"\1\154",
			"\1\155",
			"\1\156",
			"\1\157",
			"\1\160",
			"\1\161",
			"\1\162",
			"\1\163",
			"\1\164",
			"\1\165",
			"\1\166",
			"\1\167",
			"\1\170",
			"\1\171",
			"\1\172",
			"\1\173",
			"\1\174",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54\74\uffff\1\54\10\uffff"+
			"\27\54\1\uffff\37\54\1\uffff\u0286\54\1\uffff\u1c81\54\14\uffff\2\54"+
			"\61\uffff\2\54\57\uffff\u0120\54\u0a70\uffff\u03f0\54\21\uffff\ua7ff"+
			"\54\u2100\uffff\u04d0\54\40\uffff\u010f\54\1\uffff\u00fe\54",
			"\1\176",
			"\1\177",
			"\1\u0080",
			"\1\u0081",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54\74\uffff\1\54\10\uffff"+
			"\27\54\1\uffff\37\54\1\uffff\u0286\54\1\uffff\u1c81\54\14\uffff\2\54"+
			"\61\uffff\2\54\57\uffff\u0120\54\u0a70\uffff\u03f0\54\21\uffff\ua7ff"+
			"\54\u2100\uffff\u04d0\54\40\uffff\u010f\54\1\uffff\u00fe\54",
			"\1\u0083",
			"\1\u0084",
			"\1\u0085",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54\74\uffff\1\54\10\uffff"+
			"\27\54\1\uffff\37\54\1\uffff\u0286\54\1\uffff\u1c81\54\14\uffff\2\54"+
			"\61\uffff\2\54\57\uffff\u0120\54\u0a70\uffff\u03f0\54\21\uffff\ua7ff"+
			"\54\u2100\uffff\u04d0\54\40\uffff\u010f\54\1\uffff\u00fe\54",
			"\1\u0087",
			"\1\u0088",
			"\1\u0089",
			"\1\u008a",
			"",
			"\1\u008b",
			"\2\u008c\1\uffff\2\u008c\22\uffff\1\u008c\132\uffff\1\u008c",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54\74\uffff\1\54\10\uffff"+
			"\27\54\1\uffff\37\54\1\uffff\u0286\54\1\uffff\u1c81\54\14\uffff\2\54"+
			"\61\uffff\2\54\57\uffff\u0120\54\u0a70\uffff\u03f0\54\21\uffff\ua7ff"+
			"\54\u2100\uffff\u04d0\54\40\uffff\u010f\54\1\uffff\u00fe\54",
			"\1\u008e",
			"",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54\74\uffff\1\54\10\uffff"+
			"\27\54\1\uffff\37\54\1\uffff\u0286\54\1\uffff\u1c81\54\14\uffff\2\54"+
			"\61\uffff\2\54\57\uffff\u0120\54\u0a70\uffff\u03f0\54\21\uffff\ua7ff"+
			"\54\u2100\uffff\u04d0\54\40\uffff\u010f\54\1\uffff\u00fe\54",
			"\1\u0090",
			"\1\u0091",
			"",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54\74\uffff\1\54\10\uffff"+
			"\27\54\1\uffff\37\54\1\uffff\u0286\54\1\uffff\u1c81\54\14\uffff\2\54"+
			"\61\uffff\2\54\57\uffff\u0120\54\u0a70\uffff\u03f0\54\21\uffff\ua7ff"+
			"\54\u2100\uffff\u04d0\54\40\uffff\u010f\54\1\uffff\u00fe\54",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54\74\uffff\1\54\10\uffff"+
			"\27\54\1\uffff\37\54\1\uffff\u0286\54\1\uffff\u1c81\54\14\uffff\2\54"+
			"\61\uffff\2\54\57\uffff\u0120\54\u0a70\uffff\u03f0\54\21\uffff\ua7ff"+
			"\54\u2100\uffff\u04d0\54\40\uffff\u010f\54\1\uffff\u00fe\54",
			"\1\u0094",
			"\1\u0095",
			"\2\u0096\1\uffff\2\u0096\22\uffff\1\u0096\132\uffff\1\u0096",
			"",
			"",
			"\1\u0097",
			"",
			"\1\u0098",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54\74\uffff\1\54\10\uffff"+
			"\27\54\1\uffff\37\54\1\uffff\u0286\54\1\uffff\u1c81\54\14\uffff\2\54"+
			"\61\uffff\2\54\57\uffff\u0120\54\u0a70\uffff\u03f0\54\21\uffff\ua7ff"+
			"\54\u2100\uffff\u04d0\54\40\uffff\u010f\54\1\uffff\u00fe\54",
			"",
			"",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54\74\uffff\1\54\10\uffff"+
			"\27\54\1\uffff\37\54\1\uffff\u0286\54\1\uffff\u1c81\54\14\uffff\2\54"+
			"\61\uffff\2\54\57\uffff\u0120\54\u0a70\uffff\u03f0\54\21\uffff\ua7ff"+
			"\54\u2100\uffff\u04d0\54\40\uffff\u010f\54\1\uffff\u00fe\54",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54\74\uffff\1\54\10\uffff"+
			"\27\54\1\uffff\37\54\1\uffff\u0286\54\1\uffff\u1c81\54\14\uffff\2\54"+
			"\61\uffff\2\54\57\uffff\u0120\54\u0a70\uffff\u03f0\54\21\uffff\ua7ff"+
			"\54\u2100\uffff\u04d0\54\40\uffff\u010f\54\1\uffff\u00fe\54",
			"",
			"\2\u009c\1\uffff\2\u009c\22\uffff\1\u009c\132\uffff\1\u009c",
			"\12\54\7\uffff\32\54\4\uffff\1\54\1\uffff\32\54\74\uffff\1\54\10\uffff"+
			"\27\54\1\uffff\37\54\1\uffff\u0286\54\1\uffff\u1c81\54\14\uffff\2\54"+
			"\61\uffff\2\54\57\uffff\u0120\54\u0a70\uffff\u03f0\54\21\uffff\ua7ff"+
			"\54\u2100\uffff\u04d0\54\40\uffff\u010f\54\1\uffff\u00fe\54",
			"",
			"",
			"",
			"",
			""
	};

	static final short[] DFA32_eot = DFA.unpackEncodedString(DFA32_eotS);
	static final short[] DFA32_eof = DFA.unpackEncodedString(DFA32_eofS);
	static final char[] DFA32_min = DFA.unpackEncodedStringToUnsignedChars(DFA32_minS);
	static final char[] DFA32_max = DFA.unpackEncodedStringToUnsignedChars(DFA32_maxS);
	static final short[] DFA32_accept = DFA.unpackEncodedString(DFA32_acceptS);
	static final short[] DFA32_special = DFA.unpackEncodedString(DFA32_specialS);
	static final short[][] DFA32_transition;

	static {
		int numStates = DFA32_transitionS.length;
		DFA32_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA32_transition[i] = DFA.unpackEncodedString(DFA32_transitionS[i]);
		}
	}

	protected class DFA32 extends DFA {

		public DFA32(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 32;
			this.eot = DFA32_eot;
			this.eof = DFA32_eof;
			this.min = DFA32_min;
			this.max = DFA32_max;
			this.accept = DFA32_accept;
			this.special = DFA32_special;
			this.transition = DFA32_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( COMMENT | ARG_OR_CHARSET | ACTION | OPTIONS | TOKENS_SPEC | CHANNELS | IMPORT | FRAGMENT | LEXER | PARSER | GRAMMAR | RETURNS | LOCALS | THROWS | CATCH | FINALLY | MODE | COLON | COLONCOLON | COMMA | SEMI | LPAREN | RPAREN | RARROW | LT | GT | ASSIGN | QUESTION | STAR | PLUS | PLUS_ASSIGN | OR | DOLLAR | DOT | RANGE | AT | POUND | NOT | RBRACE | ID | INT | STRING_LITERAL | WS | UnicodeBOM | ERRCHAR );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA32_0 = input.LA(1);
						s = -1;
						if ( (LA32_0=='/') ) {s = 1;}
						else if ( (LA32_0=='[') ) {s = 2;}
						else if ( (LA32_0=='{') ) {s = 3;}
						else if ( (LA32_0=='o') ) {s = 4;}
						else if ( (LA32_0=='t') ) {s = 5;}
						else if ( (LA32_0=='c') ) {s = 6;}
						else if ( (LA32_0=='i') ) {s = 7;}
						else if ( (LA32_0=='f') ) {s = 8;}
						else if ( (LA32_0=='l') ) {s = 9;}
						else if ( (LA32_0=='p') ) {s = 10;}
						else if ( (LA32_0=='g') ) {s = 11;}
						else if ( (LA32_0=='r') ) {s = 12;}
						else if ( (LA32_0=='m') ) {s = 13;}
						else if ( (LA32_0==':') ) {s = 14;}
						else if ( (LA32_0==',') ) {s = 15;}
						else if ( (LA32_0==';') ) {s = 16;}
						else if ( (LA32_0=='(') ) {s = 17;}
						else if ( (LA32_0==')') ) {s = 18;}
						else if ( (LA32_0=='-') ) {s = 19;}
						else if ( (LA32_0=='<') ) {s = 20;}
						else if ( (LA32_0=='>') ) {s = 21;}
						else if ( (LA32_0=='=') ) {s = 22;}
						else if ( (LA32_0=='?') ) {s = 23;}
						else if ( (LA32_0=='*') ) {s = 24;}
						else if ( (LA32_0=='+') ) {s = 25;}
						else if ( (LA32_0=='|') ) {s = 26;}
						else if ( (LA32_0=='$') ) {s = 27;}
						else if ( (LA32_0=='.') ) {s = 28;}
						else if ( (LA32_0=='@') ) {s = 29;}
						else if ( (LA32_0=='#') ) {s = 30;}
						else if ( (LA32_0=='~') ) {s = 31;}
						else if ( (LA32_0=='}') ) {s = 32;}
						else if ( ((LA32_0 >= 'A' && LA32_0 <= 'Z')||(LA32_0 >= 'a' && LA32_0 <= 'b')||(LA32_0 >= 'd' && LA32_0 <= 'e')||LA32_0=='h'||(LA32_0 >= 'j' && LA32_0 <= 'k')||LA32_0=='n'||LA32_0=='q'||LA32_0=='s'||(LA32_0 >= 'u' && LA32_0 <= 'z')||(LA32_0 >= '\u00C0' && LA32_0 <= '\u00D6')||(LA32_0 >= '\u00D8' && LA32_0 <= '\u00F6')||(LA32_0 >= '\u00F8' && LA32_0 <= '\u02FF')||(LA32_0 >= '\u0370' && LA32_0 <= '\u037D')||(LA32_0 >= '\u037F' && LA32_0 <= '\u1FFF')||(LA32_0 >= '\u200C' && LA32_0 <= '\u200D')||(LA32_0 >= '\u2070' && LA32_0 <= '\u218F')||(LA32_0 >= '\u2C00' && LA32_0 <= '\u2FEF')||(LA32_0 >= '\u3001' && LA32_0 <= '\uD7FF')||(LA32_0 >= '\uF900' && LA32_0 <= '\uFDCF')||(LA32_0 >= '\uFDF0' && LA32_0 <= '\uFEFE')||(LA32_0 >= '\uFF00' && LA32_0 <= '\uFFFD')) ) {s = 33;}
						else if ( ((LA32_0 >= '0' && LA32_0 <= '9')) ) {s = 34;}
						else if ( (LA32_0=='\'') ) {s = 35;}
						else if ( ((LA32_0 >= '\t' && LA32_0 <= '\n')||(LA32_0 >= '\f' && LA32_0 <= '\r')||LA32_0==' ') ) {s = 36;}
						else if ( (LA32_0=='\uFEFF') ) {s = 37;}
						else if ( ((LA32_0 >= '\u0000' && LA32_0 <= '\b')||LA32_0=='\u000B'||(LA32_0 >= '\u000E' && LA32_0 <= '\u001F')||(LA32_0 >= '!' && LA32_0 <= '\"')||(LA32_0 >= '%' && LA32_0 <= '&')||(LA32_0 >= '\\' && LA32_0 <= '`')||(LA32_0 >= '\u007F' && LA32_0 <= '\u00BF')||LA32_0=='\u00D7'||LA32_0=='\u00F7'||(LA32_0 >= '\u0300' && LA32_0 <= '\u036F')||LA32_0=='\u037E'||(LA32_0 >= '\u2000' && LA32_0 <= '\u200B')||(LA32_0 >= '\u200E' && LA32_0 <= '\u206F')||(LA32_0 >= '\u2190' && LA32_0 <= '\u2BFF')||(LA32_0 >= '\u2FF0' && LA32_0 <= '\u3000')||(LA32_0 >= '\uD800' && LA32_0 <= '\uF8FF')||(LA32_0 >= '\uFDD0' && LA32_0 <= '\uFDEF')||(LA32_0 >= '\uFFFE' && LA32_0 <= '\uFFFF')) ) {s = 38;}
						if ( s>=0 ) return s;
						break;
					case 1 : 
						int LA32_2 = input.LA(1);
						 
						int index32_2 = input.index();
						input.rewind();
						s = -1;
						if ( ((LA32_2 >= '\u0000' && LA32_2 <= '\t')||(LA32_2 >= '\u000B' && LA32_2 <= '\f')||(LA32_2 >= '\u000E' && LA32_2 <= '\uFFFF')) && (((!isLexerRule)||(isLexerRule)))) {s = 40;}
						else if ( (LA32_2=='\n'||LA32_2=='\r') && ((!isLexerRule))) {s = 41;}
						else s = 38;
						 
						input.seek(index32_2);
						if ( s>=0 ) return s;
						break;
			}
			if (state.backtracking>0) {state.failed=true; return -1;}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 32, _s, input);
			error(nvae);
			throw nvae;
		}
	}

}
