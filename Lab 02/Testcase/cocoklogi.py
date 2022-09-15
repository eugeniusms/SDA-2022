jml_output = int(input("JUMLAH OUTPUT: "))

# orang pertama
print("DAFTAR OUTPUT ORANG PERTAMA: ")
lst_a = list()
for i in range(jml_output):
    output_a = int(input())
    lst_a.append(output_a)

# orang kedua
print("DAFTAR OUTPUT ORANG KEDUA: ")
lst_b = list()
for i in range(jml_output):
    output_b = int(input())
    lst_b.append(output_b)

# cocokkan
jml_cocok = 0
print("\nDAFTAR OUTPUT: \n")
for i in range(jml_output):
    if lst_a[i] == lst_b[i]:
        print(f"{lst_a[i]} | {lst_b[i]} : COCOK!")
        jml_cocok += 1
    else:
        print(f"{lst_a[i]} | {lst_b[i]} : TIDAK COCOK!")

print(f"\nYANG COCOK: {jml_cocok}/{jml_output} ({jml_cocok/jml_output*100}%)")