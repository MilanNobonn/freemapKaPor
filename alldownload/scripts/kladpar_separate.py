#!/usr/bin/python
# -*- coding: utf-8 -*-

import re
import sys
import psycopg2

def push_data(wcur, arr):
    print ("Push data")
    wcur.executemany(
      "INSERT INTO kladpar_popisky_sep VALUES(%s, %s, %s, %s, %s, %s, %s);", 
      arr
    )
    

def main():
  conn = psycopg2.connect(sys.argv[1])

  wcur = conn.cursor()
  rcur = conn.cursor("rcur")

  rcur.execute("""SELECT "key", "name" FROM kladpar_popisky""")

  separator_pattern = re.compile("[^\n]+: ")

  arr = []

  for key, name in rcur:
    name = name.replace("\\n", "\n")
    params = dict()
    #print (key,name)

    parts = [(s.start(), s.end())
             for s in separator_pattern.finditer(name)]
    for i in range(len(parts)):

      k = name[parts[i][0] : parts[i][1]]

      if i < len(parts) - 1:
        v = name[parts[i][1]: parts[i + 1][0]]
      else:
        v = name[parts[i][1]: ]

      v = v.replace("\n", " ")
      k = k.strip()
      v = v.strip()

      if v == "":
        v = None
    
      params[k] = v
      
      #print k
   
    parcela = params["Parcela:"]
    if parcela.find('/') > 0:
        parcela1, parcela2 = parcela.split("/")
    else:
        parcela1 = parcela
        parcela2 = None
    
    arr.append((key, 
                int(parcela1), parcela2, params['Výmera:'], 
                params['Druh pozemku:'], params['Využitie pozemku:'],
                params['Číslo LV:']))
    
    if len(arr) > 15000:
        push_data(wcur, arr)
        arr = []
    
  push_data(wcur, arr)

  conn.commit()
  print ("Done")

main()


