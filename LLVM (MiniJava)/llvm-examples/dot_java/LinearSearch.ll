@.LS_vtable = global [4 x i8*] [
	i8* bitcast (i32 (i8*, i32)* @LS.Start to i8*),
	i8* bitcast (i32 (i8*)* @LS.Print to i8*),
	i8* bitcast (i32 (i8*, i32)* @LS.Search to i8*),
	i8* bitcast (i32 (i8*, i32)* @LS.Init to i8*)
]

declare i8* @calloc(i32, i32)
declare i32 @printf(i8*, ...)
declare void @exit(i32)

@_cint = constant [4 x i8] c"%d\0a\00" 
@_cOOB = constant [15 x i8] c"Out of bounds\0a\00"

define void @print_int(i32 %i) {
	%_str = bitcast [4 x i8]* @_cint to i8*
	call i32 (i8*, ...) @printf(i8* %_str, i32 %i)
	ret void
}

define void @throw_oob() {
	%_str = bitcast [15 x i8]* @_cOOB to i8*
	call i32 (i8*, ...) @printf(i8* %_str)
	call void @exit(i32 1)
	ret void
}

define i32 @main() {


	; Allocating object type LS
	%_0 = call i8* @calloc(i32 1, i32 20)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [4 x i8*], [4 x i8*]* @.LS_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	; LS.Start(): 0
	%_3 = bitcast i8* %_0 to i8***
	%_4 = load i8**, i8*** %_3
	%_5 = getelementptr i8*, i8** %_4, i32 0
	%_6 = load i8*, i8** %_5
	%_7 = bitcast i8* %_6 to i32 (i8*, i32)*
	%_8 = call i32 %_7(i8* %_0,i32 10)

	call void (i32) @print_int(i32 %_8)

	ret i32 0
}

define i32 @LS.Start(i8* %this, i32 %.sz) {

	%sz = alloca i32
	store i32 %.sz, i32* %sz

	%aux01 = alloca i32
	%aux02 = alloca i32

	; LS.Init(): 3
	%_0 = bitcast i8* %this to i8***
	%_1 = load i8**, i8*** %_0
	%_2 = getelementptr i8*, i8** %_1, i32 3
	%_3 = load i8*, i8** %_2
	%_4 = bitcast i8* %_3 to i32 (i8*, i32)*
	%_5 = load i32, i32* %sz
	%_6 = call i32 %_4(i8* %this,i32 %_5)
	store i32 %_6, i32* %aux01

	; LS.Print(): 1
	%_7 = bitcast i8* %this to i8***
	%_8 = load i8**, i8*** %_7
	%_9 = getelementptr i8*, i8** %_8, i32 1
	%_10 = load i8*, i8** %_9
	%_11 = bitcast i8* %_10 to i32 (i8*)*
	%_12 = call i32 %_11(i8* %this)
	store i32 %_12, i32* %aux02


	call void (i32) @print_int(i32 9999)
	; LS.Search(): 2
	%_13 = bitcast i8* %this to i8***
	%_14 = load i8**, i8*** %_13
	%_15 = getelementptr i8*, i8** %_14, i32 2
	%_16 = load i8*, i8** %_15
	%_17 = bitcast i8* %_16 to i32 (i8*, i32)*
	%_18 = call i32 %_17(i8* %this,i32 8)

	call void (i32) @print_int(i32 %_18)
	; LS.Search(): 2
	%_19 = bitcast i8* %this to i8***
	%_20 = load i8**, i8*** %_19
	%_21 = getelementptr i8*, i8** %_20, i32 2
	%_22 = load i8*, i8** %_21
	%_23 = bitcast i8* %_22 to i32 (i8*, i32)*
	%_24 = call i32 %_23(i8* %this,i32 12)

	call void (i32) @print_int(i32 %_24)
	; LS.Search(): 2
	%_25 = bitcast i8* %this to i8***
	%_26 = load i8**, i8*** %_25
	%_27 = getelementptr i8*, i8** %_26, i32 2
	%_28 = load i8*, i8** %_27
	%_29 = bitcast i8* %_28 to i32 (i8*, i32)*
	%_30 = call i32 %_29(i8* %this,i32 17)

	call void (i32) @print_int(i32 %_30)
	; LS.Search(): 2
	%_31 = bitcast i8* %this to i8***
	%_32 = load i8**, i8*** %_31
	%_33 = getelementptr i8*, i8** %_32, i32 2
	%_34 = load i8*, i8** %_33
	%_35 = bitcast i8* %_34 to i32 (i8*, i32)*
	%_36 = call i32 %_35(i8* %this,i32 50)

	call void (i32) @print_int(i32 %_36)

	ret i32 55

}

define i32 @LS.Print(i8* %this) {

	%j = alloca i32

	store i32 1, i32* %j

	; While Statment
	br label %loop0
loop0:
	%_0 = getelementptr i8, i8* %this, i32 16
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %_1
	%_3 = load i32, i32* %j
	%_4 = icmp slt i32 %_3, %_2
	br i1 %_4, label %statments1, label %exitwhile2
statments1:
	; Array Lookup
	%_5 = getelementptr i8, i8* %this, i32 8
	%_6 = bitcast i8* %_5 to i32**
	%_7 = load i32*, i32** %_6
	%_8 = load i32, i32* %j
	%_9 = load i32, i32* %_7
	%_10 = icmp ult i32 %_8, %_9
	br i1 %_10, label %oob0, label %oob1

oob0:
	%_11 = add i32 %_8, 1
	%_12 = getelementptr i32, i32* %_7, i32 %_11
	%_13 = load i32, i32* %_12
	br label %oob2

oob1:
	call void @throw_oob()
	br label %oob2

oob2:

	call void (i32) @print_int(i32 %_13)
	%_14 = load i32, i32* %j
	%_15 = add i32 %_14, 1
	store i32 %_15, i32* %j

	br label %loop0
exitwhile2:

	ret i32 0

}

define i32 @LS.Search(i8* %this, i32 %.num) {

	%num = alloca i32
	store i32 %.num, i32* %num

	%j = alloca i32
	%ls01 = alloca i1
	%ifound = alloca i32
	%aux01 = alloca i32
	%aux02 = alloca i32
	%nt = alloca i32

	store i32 1, i32* %j

	store i1 0, i1* %ls01

	store i32 0, i32* %ifound

	; While Statment
	br label %loop3
loop3:
	%_0 = getelementptr i8, i8* %this, i32 16
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %_1
	%_3 = load i32, i32* %j
	%_4 = icmp slt i32 %_3, %_2
	br i1 %_4, label %statments4, label %exitwhile5
statments4:
	; Array Lookup
	%_5 = getelementptr i8, i8* %this, i32 8
	%_6 = bitcast i8* %_5 to i32**
	%_7 = load i32*, i32** %_6
	%_8 = load i32, i32* %j
	%_9 = load i32, i32* %_7
	%_10 = icmp ult i32 %_8, %_9
	br i1 %_10, label %oob3, label %oob4

oob3:
	%_11 = add i32 %_8, 1
	%_12 = getelementptr i32, i32* %_7, i32 %_11
	%_13 = load i32, i32* %_12
	br label %oob5

oob4:
	call void @throw_oob()
	br label %oob5

oob5:
	store i32 %_13, i32* %aux01

	%_14 = load i32, i32* %num
	%_15 = add i32 %_14, 1
	store i32 %_15, i32* %aux02

	; If Statment
	%_16 = load i32, i32* %aux01
	%_17 = load i32, i32* %num
	%_18 = icmp slt i32 %_16, %_17
	br i1 %_18, label %iflabel6, label %ellabel7

iflabel6:
	store i32 0, i32* %nt

	br label %exitif8

ellabel7:
	; If Statment
	%_19 = load i32, i32* %aux01
	%_20 = load i32, i32* %aux02
	%_21 = icmp slt i32 %_19, %_20
	%_22 = xor i1 1, %_21
	br i1 %_22, label %iflabel9, label %ellabel10

iflabel9:
	store i32 0, i32* %nt

	br label %exitif11

ellabel10:
	store i1 1, i1* %ls01

	store i32 1, i32* %ifound

	%_23 = getelementptr i8, i8* %this, i32 16
	%_24 = bitcast i8* %_23 to i32*
	%_25 = load i32, i32* %_24
	store i32 %_25, i32* %j

	br label %exitif11

exitif11:

	br label %exitif8

exitif8:

	%_26 = load i32, i32* %j
	%_27 = add i32 %_26, 1
	store i32 %_27, i32* %j

	br label %loop3
exitwhile5:
	%_28 = load i32, i32* %ifound

	ret i32 %_28

}

define i32 @LS.Init(i8* %this, i32 %.sz) {

	%sz = alloca i32
	store i32 %.sz, i32* %sz

	%j = alloca i32
	%k = alloca i32
	%aux01 = alloca i32
	%aux02 = alloca i32

	%_0 = getelementptr i8, i8* %this, i32 16
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %sz
	store i32 %_2, i32* %_1

	; Int Array Allocation
	%_3 = load i32, i32* %sz
	%_4 = icmp slt i32 %_3, 0
	br i1 %_4, label %arr_alloc12, label %arr_alloc13

arr_alloc12:
	call void @throw_oob()
	br label %arr_alloc13

arr_alloc13:
	%_5 = add i32 %_3, 1
	%_6 = call i8* @calloc(i32 4, i32 %_5)
	%_7 = bitcast i8* %_6 to i32*
	store i32 %_3, i32* %_7

	%_8 = getelementptr i8, i8* %this, i32 8
	%_9 = bitcast i8* %_8 to i32**
	store i32* %_7, i32** %_9

	store i32 1, i32* %j

	%_10 = getelementptr i8, i8* %this, i32 16
	%_11 = bitcast i8* %_10 to i32*
	%_12 = load i32, i32* %_11
	%_13 = add i32 %_12, 1
	store i32 %_13, i32* %k

	; While Statment
	br label %loop6
loop6:
	%_14 = getelementptr i8, i8* %this, i32 16
	%_15 = bitcast i8* %_14 to i32*
	%_16 = load i32, i32* %_15
	%_17 = load i32, i32* %j
	%_18 = icmp slt i32 %_17, %_16
	br i1 %_18, label %statments7, label %exitwhile8
statments7:
	%_19 = load i32, i32* %j
	%_20 = mul i32 2, %_19
	store i32 %_20, i32* %aux01

	%_21 = load i32, i32* %k
	%_22 = sub i32 %_21, 3
	store i32 %_22, i32* %aux02

	; Array Assignment
	%_23 = getelementptr i8, i8* %this, i32 8
	%_24 = bitcast i8* %_23 to i32**
	%_25 = load i32*, i32** %_24
	%_26 = load i32, i32* %j
	%_27 = load i32, i32* %_25
	%_28 = icmp ult i32 %_26, %_27
	br i1 %_28, label %oob14, label %oob15

oob14:
	%_29 = add i32 %_26, 1
	%_30 = getelementptr i32, i32* %_25, i32 %_29
	%_31 = load i32, i32* %aux01
	%_32 = load i32, i32* %aux02
	%_33 = add i32 %_31, %_32
	store i32 %_33, i32* %_30
	br label %oob16

oob15:
	call void @throw_oob()
	br label %oob16

oob16:
	%_34 = load i32, i32* %j
	%_35 = add i32 %_34, 1
	store i32 %_35, i32* %j

	%_36 = load i32, i32* %k
	%_37 = sub i32 %_36, 1
	store i32 %_37, i32* %k

	br label %loop6
exitwhile8:

	ret i32 0

}

