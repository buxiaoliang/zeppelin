package com.nflabs.zeppelin.cli;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.nflabs.zeppelin.result.ResultDataException;
import com.nflabs.zeppelin.result.ResultDataStream;
import com.nflabs.zeppelin.zengine.Z;
import com.nflabs.zeppelin.zengine.ZException;
import com.nflabs.zeppelin.zengine.ZQL;
import com.nflabs.zeppelin.zengine.ZQLException;

public class ZeppelinCli {
	public static void main(String args[]) throws ParseException, ZException, IOException, ZQLException, SQLException, ResultDataException{
	
		Options options = new Options();
		options.addOption(OptionBuilder.withArgName("File")
									   .withDescription("Run ZQL in given text file")
									   .hasArg(true)
									   .create("f"));
		options.addOption(OptionBuilder.withArgName("ZQLStatement")
				   .withDescription("Run ZQL statement")
				   .hasArg(true)
				   .create("e"));		
		options.addOption(OptionBuilder.withDescription("See this messages")
									   .withLongOpt("help")
									   .create("h"));
									 
				
		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);
		
		if(cmd.hasOption("f")){
			ZQL zql = new ZQL();
			zql.load(new File(cmd.getOptionValue("f")));
			List<Z> zs = zql.eval();
			
			
			for(Z z : zs){
				List<ResultSet> results = z.execute();
				for(ResultSet r : results){
					new ResultDataStream(r, System.out).load();
				}
			}
		} else if(cmd.hasOption("e")){ 			
			ZQL zql = new ZQL(cmd.getOptionValue("e"));
			List<Z> zs = zql.eval();			
			
			for(Z z : zs){
				List<ResultSet> results = z.execute();
				for(ResultSet r : results){
					new ResultDataStream(r, System.out).load();
				}
			}
		} else if(cmd.hasOption("h")){
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ZeppelinCli", options);
		} 
		

	}
}
