addi $t1, $zero, 4
add $s1,$t1,$zero
#slt $t1,$zero,$t1
jump:
    sw  $t1, 0($zero)

bne $t1, $zero, jump