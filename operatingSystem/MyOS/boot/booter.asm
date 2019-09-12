; MyOS
    org 07c00H
    mov ax, cs     ; 初始化寄存器
    mov ds, ax
    mov es, ax
    call DisplayStr ; 显示字符串
    jmp $           ; CPU空转
DisplayStr:
    mov ax, BootMessage
    mov bp, ax     ; ES:BP = 串地址
    mov cx, 21     ; CX = 串长度
    mov ax, 01301h ; AH = 13, AL = 01h
    mov bx, 000ch  ; 页号为0 (BH = 0) 黑底红字(BL = 0Ch,高亮)
    mov dl, 0      ; 第0列开始显示
    int 10h        ; 调用10h号中断进行显示
    ret
BootMessage: db "MyOS say Hello world!"
    times 510 - ($ - $$) db 0 ; 使用0进行填充，以便生成的二进制代码恰好为512字节
    dw 0xaa55                 ; 启动扇区规定的结束标志