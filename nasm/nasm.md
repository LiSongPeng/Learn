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

