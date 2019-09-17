# NASM Assembly Language
## 1. NASM语法
### 1.1 伪指令
#### 1.1.1 初始化声明伪指令
 - **DB** ，**DW** , **DD** , **DQ** , **DT** , **DO** , **DY** and **DZ**
- **db 0x55** ** ; just the byte 0x55
- **db 0x55,0x56,0x57** ** ; three bytes in succession
- **db ’a’,0x55** ; character constants are OK
- **db ’hello’,13,10,’$’** ; so are string constants
- **dw 0x1234** ; 0x34 0x12
- **dw ’a’** ; 0x61 0x00 (it’s just a number)
- **dw ’ab’** ; 0x61 0x62 (character constant)
- **dw ’abc’** ; 0x61 0x62 0x63 0x00 (string)
- **dd 0x12345678** ; 0x78 0x56 0x34 0x12
- **dd 1.234567e20** ; floating−point constant
- **dq 0x123456789abcdef0** ; eight byte constant
- **dq 1.234567e20** ; double−precision float
- **dt 1.234567e20** ; extended−precision float
#### 1.1.2 保留字声明伪指令
- **RESB**, **RESW**, **RESD**, **RESQ**, **REST**, **RESO**, **RESY** and **RESZ**
- **buffer: resb 64** ; reserve 64 bytes
- **wordvar: resw 1** ; reserve a word
- **realarray resq 10** ; array of ten reals
- **ymmval: resy 1** ; one YMM register
- **zmmvals: resz 32** ; 32 ZMM registers
#### 1.1.3 INCBIN 导入命令
- **incbin "file.dat"** ; include the whole file
- **incbin "file.dat",1024** ; skip the first 1024 bytes
- **incbin "file.dat",1024,512** ; skip the first 1024, and actually include at most 512
#### 1.1.4  EQU 常量定义命令
- **message db 'hello world'**
  **msglen equ $ - message**
#### 1.1.5  TIMES 指令或数据重复命令
- **zerobuf: times 64 db 0**
- **buffer: db 'hello world'**
  **times 64 - $ + buffer db ' '**
- **times 100 movesb**
### 1.2 有效地址
#### 1.2.1 有效地址示例
- **wordvar dw 123**
  **mov ax, [wordvar]**
  **mov ax, [wordvar + 1]**
  **mov ax, [es:wordvar + bx]** 间接寻址默认段寄存器为ds，此处强制使用es作为段寄存器
- **mov eax, [ebx * 5]** ; 等价于 [ebx * 4 + ebx]
  **mov eax, [label * 2 - label2]** ; ie [label1 + (label1 - label2)]
### 1.3 常量
#### 1.3.1 可数常量
- 规则
1. 支持二进制、八进制、十进制、十六进制
2. 长数字字符串可用_分割
- 示例
<br/>
**mov ax,200** ; decimal
<br/>
**mov ax,0200** ; still decimal
<br/>
**mov ax,0200d** ; explicitly decimal
<br/>
**mov ax,0d200** ; also decimal
<br/>
**mov ax,0c8h** ; hex
<br/>
**mov ax,$0c8** ; hex again: the 0 is required
<br/>
**mov ax,0xc8** ; hex yet again
<br/>
**mov ax,0hc8** ; still hex
<br/>
**mov ax,310q** ; octal
<br/>
**mov ax,310o** ; octal again
<br/>
**mov ax,0o310** ; octal yet again
<br/>
**mov ax,0q310** ; octal yet again
<br/>
**mov ax,11001000b** ; binary
<br/>
**mov ax,1100_1000b** ; same binary constant
<br/>
**mov ax,1100_1000y** ; same binary constant once more
<br/>
**mov ax,0b1100_1000** ; same binary constant yet again
<br/>
**mov ax,0y1100_1000** ; same binary constant yet again
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
**mov eax, 'adcd'** 因为是小端存储，存到内存中会是0x64636261而不是0x61626364
#### 1.3.3 字符串常量
- 规则
1. 在伪指令上下文、预处理指令中使用
- 示例1
1. **db ’hello’** ; string constant
1. **db ’h’,’e’,’l’,’l’,’o’** ; equivalent character constants
- 示例2(以下全都等价) 
<br/>
**dd ’ninechars’** ; doubleword string constant
<br/>
**dd ’nine’,’char’,’s’** ; becomes three doublewords
<br/>
**db ’ninechars’,0,0,0** ; and really looks like this
