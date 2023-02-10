import random
import string

def get_random_string(length):
    # choose from all lowercase letter
    letters = string.ascii_lowercase
    result_str = ''.join(random.choice(letters) for i in range(length))
    return result_str

file_keluaran = input("Masukkan nama file output: ")
isi_file = "3\nB 7\nA 5\nC 8\n"

jml_query = int(input("Jumlah Query: "))

isi_file += str(jml_query) + "\n"

for i in range(jml_query):
    isi_file += "MASUK " + get_random_string(20) + " " + str(random.randint(0, 1000000000)) + "\n"

# Menulis text yang sudah dioperasikan ke dalam text output
my_file = open(file_keluaran, mode='w')
print(isi_file, file=my_file)
my_file.close()