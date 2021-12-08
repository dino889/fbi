// ./graphql/DB.js
import mariadb from "mariadb";
import {dbConfig} from "../config/db_config";
import {PrismaClient} from "@prisma/client";
import { typeDefs } from "graphql-scalars";
import {PythonShell, PythonShellError} from  "python-shell";
import { run } from "python-shell";

const prisma = new PrismaClient();
const path = require('path');
const fs = require('fs');

const imgURL = "http://27.113.21.252:6400/img/result/";
// MariaDB connect pool
// http://github.com/sidorares/node-mysql2
var options = {

    mode: 'text',
    pythonPath: '',
    pythonOptions: ['-u'],
    scriptPath: '',
    args: ['args_booksidpic', '/home/c2019-8950/python-test/bye/image_bookcase.jpg','target_link']
  
  };

//Database connection config
const pool = mariadb.createPool({
        host : dbConfig.host,
        user : dbConfig.user,
        port : dbConfig.port,
        password : dbConfig.password,
        database : dbConfig.database,
        waitForConnections: true,
        connectionLimit: 10,
        queueLimit: 0,
});




//회원가입(1)
export const joinUser = async(user_ID,user_password,user_name,user_phone,user_age,user_gender, user_category) =>{

    console.log("Call joinUser");

    const newuser = await prisma.uSER.create({
        data:{
            user_ID: user_ID,
            user_password:user_password,
            user_name:user_name,
            user_phone:user_phone,
            user_age:user_age,
            user_gender:user_gender,
            user_category:user_category
        }
    });

    console.log("End joinUser");

    return newuser;
}

//파이썬 실행
export const findBookInShelf = async(book_num,user_ID) => {
    
    var url = [{url}];
    const foundBook = await prisma.bOOK.findMany(
        {
            where: {book_num : (book_num)}
        }
    )
    
    options.args[0] = foundBook[0].book_side_pic
    console.log(options.args[0])
    options.args[2] = "/home/c2019-8950/바탕화면/461_fbi/img/result/"+user_ID+".jpg"
    console.log(options.args[2])
    
    try{
        await runPython().then((result)=>{
            console.log(result);
        })
        
        url.url = imgURL+user_ID+".jpg";
        return url;

    }catch(e){
        console.log("findBookInShelf error : " + e);
        
    }
    /*
    PythonShell.run('/home/c2019-8950/python-test/bye/fbiexe.py', options, function (err, results) {

    if (err) throw err;
        console.log('results: %j', results);
    });
*/    
}
const runPython = async() =>{
    console.log("enter runPython");
    const result = await Pythonrun();
    
    
    return "success";
}

//

async function Pythonrun(){
    var completed = 0;

    PythonShell.run('/home/c2019-8950/python-test/bye/fbiexe.py', options, function (err, results) {
            if (err)
                throw err;
            console.log('results: %j', results);
            
        });

        return 0;

    /*PythonShell.run('/home/c2019-8950/python-test/bye/fbiexe.py', options, function (err, results) {
        if (err)
            throw err;
        console.log('results: %j', results);
        return 0;
    });*/
}

//로그인(1)
export const loginUser = async(id,pw) =>{

    console.log("Call loginUser");

    const loginuser = await prisma.uSER.findMany(
        {
            where: {user_ID: String(id), user_password:String(pw)}
        }
    );

    console.log(loginuser);

    console.log("End loginUser");

    return loginuser;
}

//ID기준 회원정보 찾기(1)
export const getUserbyID =async(id) =>{

    console.log("Call getUserbyID");
    
    const getUser = await prisma.uSER.findOne({
        where:{user_ID:String(id)}
    });

    console.log("End getUserbyID");

    return getUser;
}

//회원 비밀번호 변경(1)
export const updateUserPW = async(id,pw) => {
    console.log("Call updateUserPW");

    const UserPW = await prisma.uSER.update({
        where:{user_ID:id},
        data:{user_password:pw}
    })

    console.log("End updateUserPW");

    return UserPW;
}
//



//회원 마음속의 저장!(1)
export const getMindbook = async(user_id) =>{
    console.log("Call getMindbook");

    const selected_user = await prisma.uSER.findOne(
        {
            where: {user_ID: String(user_id)}
        }
    )
    const mindbook = await prisma.bOOK.findOne(
        {
            where: {book_num: selected_user.user_mindbook}
        }
    );

    console.log("End getMindbook");

    return mindbook;
};

//회원 마음속의 저장 업데이트(1)
export const updateMindbook = async(user_id,book_num) =>{
    console.log("Call updateMindbook");
    const newmindbook = await prisma.uSER.update({
        where:{user_ID:String(user_id)},
        data:{ 
            BOOK:{connect:{book_num : book_num}}}
    })

    const newbook = await prisma.bOOK.findOne({
        where: {book_num : book_num}
    })

    console.log("End updateMindbook");

    return newbook;
}

//책 번호로 책정보 찾기(1)
export const getByBookNum = async(book_num) => {
    console.log("Call getByBookNum");

    const getbook = await prisma.bOOK.findOne({
        where : {book_num : book_num}
    })

    console.log("End getByBookNum");

    return getbook;
}

// Best5 도서 목록_(1)
export const mostSearchbook = async() =>{
    console.log("Call mostSearchbook");
    
    const logcount = await pool.query("select book_num, count(book_num) from fbi.LOG group by book_num order by count(book_num) DESC Limit 0,5");
    
    var bestBook = []

    for(var i = 0; i < 5; ++i){
        bestBook.push(await prisma.bOOK.findOne({
            where : {book_num : logcount[i].book_num}
        }));
    }
    
    console.log("End mostSearchbook");
    return bestBook;
}

// 해당 회원이 검색했던 도서 목록(1)
export const searchedBook = async(user_id) =>{
    console.log("Call searchedBook");

    const searchedUser = await prisma.lOG.findMany({
        where: {user_ID : String(user_id)}
    });
    const nonDuplicatedSearchedUser = removeDuplicates(searchedUser, "book_num")
    
    var foundBooks = []
    for(var i in nonDuplicatedSearchedUser){
        foundBooks.push(await prisma.bOOK.findOne({
            where : {book_num : nonDuplicatedSearchedUser[i].book_num}
        }));
    }
    
    console.log("End searchedBook");

    return foundBooks;
}

// 
/*export const DeleteSearchedBook = async(user_id) => {
    console.log("Call DeleteSearchedBook")

    const 
}*/

//중복 데이터 제거
function removeDuplicates(originalArray, prop) {
    console.log(" → Call removeDuplicates");

    var newArray = [];
    var lookupObject  = {};

    for(var i in originalArray) {
       lookupObject[originalArray[i][prop]] = originalArray[i];
    }
    for(i in lookupObject) {
        newArray.push(lookupObject[i]);
    }

    console.log(" ← End removeDuplicates");

     return newArray;
}

// 로그 기록(1)
export const addLog = async(book_num,user_ID,user_age,book_category) => {
    console.log("Call addLog");

    const newlog = await prisma.lOG.create({
            data:{
                USER: {connect: {user_ID:user_ID}},
                user_age:user_age,
                BOOK_BOOKToLOG_book_category: {connect :{book_category:book_category}},
                BOOK_BOOKToLOG_book_num: { connect :{book_num:book_num}}
        }
    });
    const prisma = new PrismaClient();
    return newlog;
}

//책 목록 모두 가져오기(1)
export const getBooklist = async() =>{
    console.log("Call getBooklist");

    const booklist = await prisma.bOOK.findMany();

    console.log("End getBooklist");

    return booklist;
};
//회원 목록 모두 가져오기(1)
export const getAllUser = async() =>{
    console.log("Call getAllUser");

    const alluserlist = await prisma.uSER.findMany();

    console.log("End getAllUser");

    return alluserlist;
};
//회원의 책정보 가져오기(1)
export const getUserBook = async() =>{
    console.log("Call getUserBook");

    const userbook = await prisma.user_BOOK.findMany();

    console.log("End getUserBook");

    return userbook;
};
//모든 책 카테고리정보 가져오기(1)
export const getCategory = async() =>{
    console.log("Call getCategory");

    const bookcategory = await prisma.cATEGORY.findMany();

    console.log("End getCategory");

    return bookcategory;
};
// 모든 서점정보 가져오기(1)
export const getLibrary = async() =>{
    console.log("Call getLibrary");

    const librarylist = await prisma.lIBRARY.findMany();

    console.log("End getLibrary");

    return librarylist;
};
// 모든 서점의 보유목록 가져오기(1)
export const getLibraryBooks = async() =>{
    console.log("Call getLibraryBooks");

    const librarybooks = await prisma.lIB_OWN_BOOK.findMany();

    console.log("End getLibraryBooks");

    return librarybooks;
};
// 모든 로그정보 가져오기(1)
export const getlog = async() =>{
    console.log("Call getlog");

    const logdata = await prisma.lOG.findMany();

    console.log("End getlog");

    return logdata;
};
