# NASM Assembly Language
## 1. NASM语法
### 1.1 伪指令
#### 1.1.1 初始化声明伪指令
 - **DB** ，**DW** , **DD** , **DQ** , **DT** , **DO** , **DY** and **DZ**
```
db 0x55 ; just the byte 0x55
db 0x55,0x56,0x57 ; three bytes in succession
db ’a’,0x55 ; character constants are OK
db ’hello’,13,10,’$’ ; so are string constants
dw 0x1234 ; 0x34 0x12
dw ’a’ ; 0x61 0x00 (it’s just a number)
dw ’ab’ ; 0x61 0x62 (character constant)
dw ’abc’ ; 0x61 0x62 0x63 0x00 (string)
dd 0x12345678 ; 0x78 0x56 0x34 0x12
dd 1.234567e20 ; floating−point constant
dq 0x123456789abcdef0 ; eight byte constant
dq 1.234567e20 ; double−precision float
dt 1.234567e20 ; extended−precision float
```
#### 1.1.2 保留字声明伪指令
- **RESB**, **RESW**, **RESD**, **RESQ**, **REST**, **RESO**, **RESY** and **RESZ**
```
buffer: resb 64 ; reserve 64 bytes
wordvar: resw 1 ; reserve a word
realarray resq 10 ; array of ten reals
ymmval: resy 1 ; one YMM register
zmmvals: resz 32 ; 32 ZMM registers
```
#### 1.1.3 INCBIN 导入命令
```
incbin "file.dat" ; include the whole file
incbin "file.dat",1024 ; skip the first 1024 bytes
incbin "file.dat",1024,512 ; skip the first 1024, and actually include at most 512
```
#### 1.1.4  EQU 常量定义命令
```
message db 'hello world'
msglen equ $ - message
```
#### 1.1.5  TIMES 指令或数据重复命令
```
zerobuf: times 64 db 0
buffer: db 'hello world'
times 64 - $ + buffer db ' '
times 100 movesb
```
### 1.2 有效地址
#### 1.2.1 有效地址示例
```
wordvar dw 123
mov ax, [wordvar]
mov ax, [wordvar + 1]
mov ax, [es:wordvar + bx] 间接寻址默认段寄存器为ds，此处强制使用es作为段寄存器
mov eax, [ebx * 5] ; 等价于 [ebx * 4 + ebx]
mov eax, [label * 2 - label2] ; ie [label1 + (label1 - label2)]
```
### 1.3 常量
#### 1.3.1 可数常量
- 规则
1. 支持二进制、八进制、十进制、十六进制
2. 长数字字符串可用 **_** 分割
- 示例
```
mov ax,200 ; decimal
mov ax,0200 ; still decimal
mov ax,0200d ; explicitly decimal
mov ax,0d200; also decimal
mov ax,0c8h ; hex
mov ax,$0c8 ; hex again: the 0 is required
mov ax,0xc8 ; hex yet again
mov ax,0hc8 ; still hex
mov ax,310q ; octal
mov ax,310o ; octal again
mov ax,0o310 ; octal yet again
mov ax,0q310 ; octal yet again
mov ax,11001000b ; binary
mov ax,1100_1000b ; same binary constant
mov ax,1100_1000y ; same binary constant once more
mov ax,0b1100_1000 ; same binary constant yet again
mov ax,0y1100_1000 ; same binary constant yet again
```
#### 1.3.2 字符
- 规则
1. 支持转义字符 **\\**
2. 支持 **"** 、 **'** 、**`** 表示字符串
3. 支持UTF-8编码字符串
#### 1.3.3 字符常量
- 规则
1. 字符常量最高八字节长
2. 采用小端方式存储
- 示例
```
mov eax, 'adcd' ;因为是小端存储，存到内存中会是0x64636261而不是0x61626364
```
#### 1.3.3 字符串常量
- 规则
1. 在伪指令上下文、预处理指令中使用
- 示例1
```
db ’hello’ ; string constant
db ’h’,’e’,’l’,’l’,’o’ ; equivalent character constants
```
- 示例2(以下全都等价) 
```
dd ’ninechars’ ; doubleword string constant
dd ’nine’,’char’,’s’ ; becomes three doublewords
db ’ninechars’,0,0,0 ; and really looks like this
```
#### 1.3.3 浮点常量
- 规则
1. 只能用在DB、DW、DD、DQ、DT、DO或者特殊操作符 **__float8__**,**__float16__**,**__float32__**,**__float64__**,**__float80m__**,**__float80e__**,**__float128l__**,**__float128h__**
### 1.4 表达式
#### 1.4.1 | 位或操作符
#### 1.4.2 ^ 位异或操作符
#### 1.4.3 & 位与操作符
#### 1.4.4 << 和 >> 左右位移位操作符
#### 1.4.5 + 和 - 加减操作符
#### 1.4.6 *, /,//,%,%%
- * 乘法运算符
- /,// 除法运算符
-  除法运算符
- %,%% 无符号、有符号模运算符
### 1.5 段操作符(SEG)、引用段操作符(WRT)With Reference To
- SEG示例
```
mov ax,seg symbol ; 取得symbol的段基址
mov es,ax
mov bx,symbol
```
- WRT示例
```
 mov ax,weird_seg ; weird_seg is a segment base
 mov es,ax
 mov bx,symbol wrt weird_seg
```
- 跨段调用(借用语法call segment:offset)
```
call (seg procedure):procedure
call weird_seg:(procedure wrt weird_seg)
```
### 1.6 STRICT抑制优化
- with the optimizer on, and in BITS 16 mode
```
 push dword 33 ; is encoded in three bytes 66 6A 21, whereas
 push strict dword 33 ; is encoded in six bytes, with a full dword immediate operand 66 68 21 00 00 00
```
### 1.7 临界表达式
- NASM有可选多通道优化器，但是临界表达式只能用第一个通道
```
times (label−$) db 0     ; 此种形式非法，times指令属于临界表达式，只能采用定义在其之前的符号
label: db ’Where am I?’
```
### 1.8 本地标签
- 以 **.** 号开头的标签属于本地标签，本地标签意味着与之前非本地标签关联
```
label1 ; some code
.loop ; some more code
    jne .loop
    ret
label2 ; some code
.loop ; some more code
    jne .loop
    ret
```
- 本地标签会被定义成label.local_label的形式，所以可以通过以下方式进行跳转
```
label3 ; some more code
; and some more
jmp label1.loop
```
- 以 **..@** 开始的另一种宏
```
    label1: ; a non−local label
    .local: ; this is really label1.local
    ..@foo: ; this is a special symbol
    label2: ; another non−local label
    .local: ; this is really label2.local
        jmp ..@foo ; this will jump three lines up
```
## 2. NASM预处理器
### 2.1 单行宏
### 2.1.1  **%define**
- **%define** 大小写敏感
- 示例
```
%define ctrl 0x1F &
%define param(a,b) ((a)+(a)*(b))
mov byte [param(2,ebx)], ctrl ’D’
; which will expand to
mov byte [(2)+(2)*(ebx)], 0x1F & ’D’
```
- 当一个宏定义中包含对其他宏的使用时，宏展开是在调用时而不是在定义时
```
%define a(x) 1+b(x)
%define b(x) 2*x
mov ax,a(8)
; 因此b宏不会报错
```
- **%idefine** 大小写不敏感
- 解决递归调用和循环依赖
```
%define a(x) 1+a(x)
mov ax,a(3)
; 以上只会扩展一次变成1 +a(3), 以此解决递归调用的问题
```
- 宏可以重载
```
%define foo(x) 1+x
%define foo(x,y) 1+x*y
; 参数个数不同进行重载
```
### 2.1.2  **%xdefine** **%ixdefine**
```
%define isTrue 1
%define isFalse isTrue
%define isTrue 0
val1: db isFalse
%define isTrue 1
val2: db isFalse
; 结果val1 0 val2 1
; 因为%define宏在调用时展开
%xdefine isTrue 1
%xdefine isFalse isTrue
%xdefine isTrue 0
val1: db isFalse
%xdefine isTrue 1
val2: db isFalse
; 结果val1 1 val2 1
; 因为%xdefine宏在定义时展开
```
### 2.1.3  间接宏 **%[...]** 
```
%define Foo16 16
%define Foo32 32
%define Foo64 64
mov ax,Foo%[__BITS__] ; The Foo value
; __BITS__ 内嵌宏
```
```
%xdefine Bar Quux ; Expands due to %xdefine
%define Bar %[Quux] ; Expands due to %[...]
; 以上两句等效
```
### 2.1.4  宏连接指令 **%+** 
- 指令之后需要加空格
```
%define BDASTART 400h ; Start of BIOS data area
struc tBIOSDA ; its structure
.COM1addr RESW 1
.COM2addr RESW 1
; ..and so on
endstruc
mov ax,BDASTART + tBIOSDA.COM1addr
mov bx,BDASTART + tBIOSDA.COM2addr
```
```
; Macro to access BIOS variables by their names (from tBDA):
%define BDA(x) BDASTART + tBIOSDA. %+ x
mov ax,BDA(COM1addr)
mov bx,BDA(COM2addr)
```
### 2.1.5 **%?** **%??**
- **%?** 存储调用时的宏名称
- **%??** 存储定义时的宏名称
```
%idefine Foo mov %?,%??
foo
FOO
; will expand to:
mov foo,Foo
mov FOO,Foo
```
### 2.1.6 撤销宏定义 **%undef**
```
%define foo bar
%undef foo
mov eax, foo
; 会展开成mov eax, foo
```
### 2.1.7 预处理器变量 **%assign** **%iassign**
- 被用来定义没有参数并且有一个数字的值
```
%assign i i+1
```
### 2.1.8 定义字符串 **%defstr** **%idefstr**
```
%defstr test TEST
; is equivalent to
%define test ’TEST’
```
### 2.1.9 定义标志 **%deftok** **%ideftok**
```
%deftok test ’TEST’
; is equivalent to
%define test TEST
```