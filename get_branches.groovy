#!/usr/bin/groovy

def gitURL = "git repo url"
def command = "git ls-remote --heads --tags ${gitURL}"

def proc = command.execute()
proc.waitFor()              

if ( proc.exitValue() != 0 ) {
   println "Error, ${proc.err.text}"
   return -1 
}

def text = proc.in.text
# put your version string match
def match = /<REGEX>/
def tags = []

text.eachMatch(match) { tags.push(it[1]) }
tags.unique()
tags.sort( { a, b ->
         def a1 = a.tokenize('._-')
         def b1 = b.tokenize('._-')
         try {
            for (i in 1..<[a1.size(), b1.size()].min()) { 
                 if (a1[i].toInteger() != b1[i].toInteger()) return a1[i].toInteger() <=> b1[i].toInteger()
            }
            return 1
         } catch (e) {
            return -1
         }
} )
tags.reverse()
