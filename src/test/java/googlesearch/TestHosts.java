package googlesearch;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.javatuples.Pair;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

import jersey.repackaged.com.google.common.collect.Lists;

public class TestHosts {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String s = Files.toString(new File("C:/Windows/System32/drivers/etc/hosts"), Charsets.UTF_8);
		Processor p = new Processor(Pair.with("192.168.99.77", "mycom.com"));
		Files.readLines(new File("C:/Windows/System32/drivers/etc/hosts"), Charsets.UTF_8, p);
		System.out.println(s);
		System.out.println(p.getResult());
	}

	
	
	/*
# Copyright (c) 1993-2009 Microsoft Corp.
#
# This is a sample HOSTS file used by Microsoft TCP/IP for Windows.
#
# This file contains the mappings of IP addresses to host names. Each
# entry should be kept on an individual line. The IP address should
# be placed in the first column followed by the corresponding host name.
# The IP address and the host name should be separated by at least one
# space.
#
# Additionally, comments (such as these) may be inserted on individual
# lines or following the machine name denoted by a '#' symbol.
#
# For example:
#
#      102.54.94.97     rhino.acme.com          # source server
#       38.25.63.10     x.acme.com              # x client host

# localhost name resolution is handled within DNS itself.
#	127.0.0.1       localhost
#	::1             localhost
	 */
	static class Processor implements LineProcessor<List<String>>{
		
			final List<Pair<String, String>> pairs;
		
			final List<String> lines = Lists.newLinkedList();
			boolean findExampleOrNot;
			boolean canStartOrNot;
			
			Processor(final List<Pair<String, String>> pairs) {
				this.pairs = pairs;
			}
			
			Processor(final Pair<String, String> pair) {
				final List<Pair<String, String>> ll = Lists.newArrayListWithExpectedSize(1);
				ll.add(pair);
				this.pairs = Lists.newArrayList(ll);
			}

			@Override
			public boolean processLine(final String line) throws IOException {
				if(findExampleOrNot && !canStartOrNot){
					if(!line.startsWith("#")){
						canStartOrNot = true;
						lines.add("#my host start-->");
						//插入新
						pairs.forEach((pair)->{
							final String hostName = pair.getValue0();
							final String ip = pair.getValue1();
							lines.add(hostName + " " + ip);
						});
						lines.add("#my host end-->");
					}
				}
				
				if (!findExampleOrNot && line.toLowerCase().contains("example")){
					findExampleOrNot = true;
				}
				lines.add(line);
				return true;
			}

			@Override
			public List<String> getResult() {
				return lines;
			}
			
		}
}
