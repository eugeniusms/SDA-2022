lst = [21, 18, 17, 26, 22, 19]
counter = 0

def swap():
    global counter
    counter += 1 

for i in range(len(lst)-1):
    for j in range(len(lst)-i-1):
        if lst[j] > lst[j+1]:
            swap()
            lst[j], lst[j+1] = lst[j+1], lst[j]

print(lst)
print(counter)