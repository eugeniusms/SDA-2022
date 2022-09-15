import random

jml_toples = int(input())
jml_kue_per_toples = int(input())
jml_query = int(input())

daftar_query = ["GESER_KANAN", "BELI_RASA "]

# daftar kue
for toples in range(jml_toples):
    for kue in range(jml_kue_per_toples):
        print(random.randint(0,3), end=" ")
    print()

# daftar queries
for query in range(jml_query):
    perintah = daftar_query[random.randint(0,1)]
    if perintah != "GESER_KANAN":
        perintah += str(random.randint(0,5))
    print(perintah)