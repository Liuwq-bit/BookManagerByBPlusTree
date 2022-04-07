package com.example.bookmanagerbybplustree.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookmanagerbybplustree.Adapter.BookAdapter
import com.example.bookmanagerbybplustree.Book
import com.example.bookmanagerbybplustree.BookBPlusTree
import com.example.bookmanagerbybplustree.BookManager.Companion.bookBpt
import com.example.bookmanagerbybplustree.BookManager.Companion.borrowBpt
import com.example.bookmanagerbybplustree.Borrow
import com.example.bookmanagerbybplustree.R
import kotlinx.android.synthetic.main.book_list.*
import kotlinx.android.synthetic.main.list_item.*
import kotlin.concurrent.thread

/**
 * 书籍信息展示列表
 */
class BookListFragment: Fragment() {


    private lateinit var adapter: BookAdapter
    private lateinit var bookList: MutableList<Book>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val layoutManager = LinearLayoutManager(activity)
//        val layoutManager = GridLayoutManager(activity, 2)
        bookRecyclerView.layoutManager = layoutManager

        initBookBpt()

        bookList = bookBpt.showAll()
        adapter = BookAdapter(this, bookList)
        bookRecyclerView.adapter = adapter

        bookListSwipeRefresh.setColorSchemeResources(androidx.appcompat.R.color.abc_btn_colored_borderless_text_material)
        bookListSwipeRefresh.setOnRefreshListener {
            reFreshBooks(adapter)
        }

//        viewModel.bookLiveData.observe(viewLifecycleOwner, Observer { result -> // 动态查询数据
//            val books = result.getOrNull()
//            if (books != null) {
////                bookRecyclerView.visibility = View.VISIBLE
//                viewModel.bookList.clear()
//                viewModel.bookList.addAll(books)
//                if (tag1) { // 仅执行一次
//                    adapter.notifyDataSetChanged()
//                    tag1 = false
//                }
//            } else {
//                Toast.makeText(activity, "未能查询到任何书籍", Toast.LENGTH_SHORT).show()
//                result.exceptionOrNull()?.printStackTrace()
//            }
//        })

//        viewModel.markLiveData.observe(viewLifecycleOwner, Observer { result ->
//            val marks = result.getOrNull()
//            if (marks != null) {
//                viewModel.markList.clear()
//                viewModel.markList.addAll(marks)
//                if (tag2) {
//                    adapter.notifyDataSetChanged()
//                    tag2 = false
//                }
//            }
//        })
//
//        viewModel.searchAllBooks()  // 显示所有书籍
//        viewModel.searchAllMarkById(BVMApplication.USER?.user_id.toString())

    }


    private fun reFreshBooks(adapter: BookAdapter) {
        thread {
            activity?.runOnUiThread {
//                viewModel.searchAllBooks()  // 显示所有书籍
//                viewModel.searchAllMarkById(BVMApplication.USER?.user_id.toString())
//                bookList = bookBpt.showAll()
                adapter.setBookList(bookBpt.showAll())  // 更新Adapter中的list

            }
            Thread.sleep(2000)
            activity?.runOnUiThread {
//                viewModel.bookList.shuffle()    // 洗牌
                adapter.notifyDataSetChanged()
                bookListSwipeRefresh.isRefreshing = false
            }
        }

    }

    /**
     * 录入原始数据
     */
    fun initBookBpt() {
        bookBpt = BookBPlusTree()
        var book = Book(10, 10, "幻灭三部曲",
            "巴西文坛公认的伟大作家阿西斯代表作\n" +
                    "影响博尔赫斯、马尔克斯，被伍迪·艾伦、桑塔格、布鲁姆盛赞\n" +
                    "在最微妙的绝望中，对讽刺性幻灭的研究",
            "马沙多·德·阿西斯",
            "巴西最伟大的作家之一。出生于里约热内卢，只上过小学，自学掌握了法语、英语、德语和希腊语等。一生著作甚丰，涵盖诗歌、戏剧、评论、小说等领域.",
            "https://img9.doubanio.com/view/subject/l/public/s34131435.jpg")
        bookBpt.insert(book)
        book = Book(5, 5, "四万万顾客",
            "西方广告大亨传授的老上海生意经\n" +
                    "透过民国的市井街巷、柴米油盐\n" +
                    "窥见我们习以为常的文化和与今相通的人性\n" +
                    "美国国家图书奖获奖作品",
            "卡尔·克劳",
            "美国记者、商人、作家。1911年以新闻记者的身份来到上海，1918年在上海创办了克劳广告公司，较早为中国建构属于自己的广告业提供了视野。",
            "https://img2.doubanio.com/view/subject/l/public/s34129693.jpg")
        bookBpt.insert(book)
        book = Book(20, 20, "忘忧十二夜",
        "带着不同的人生困惑，有着不同收入水平、教育程度、社会地位、人生追求，又性格迥异的八个人，组成了一个名叫“八仙”的团体小组。\n" +
                "在带领者引导的团体聚会的十二个夜晚中，组员们逐渐从自己的烦恼中跳脱出来，开始了人与人之间真正自由的表达。通过这些自由声音的彼此交织，大家的关系逐渐建立、发展；",
        "李仑",
        "存在主义取向团体咨询专家\n" +
                "塔维斯托克人类关系研究所认证团体动力师、顾问\n" +
                "台湾师范大学特邀团体咨询专家",
        "https://img2.doubanio.com/view/subject/l/public/s34145893.jpg")
        bookBpt.insert(book)
        book = Book(15, 15, "满是温柔的土地上",
        "近未来世界，地球因战争遭到严重污染，人类转移到避难所中生活。此时，已冷冻睡眠数十年的天才科学家八津苏醒了，等待他的却是机器人和人类敌对的世界。",
        "阿伏伽德六",
        "日本新锐影像作家、漫画家，名字源于化学家阿伏伽德罗。在日本弹幕网站NIKONIKO上发布为VOCALOID歌曲制作的视频作品，因其独特世界观而广受好评。个人视频作品集有《鬼怪胶片》《鬼怪商品目录》。",
        "https://img9.doubanio.com/view/subject/l/public/s34127144.jpg")
        bookBpt.insert(book)
        book = Book(8, 8, "苏特里",
        "小说设定在1950年代的诺克斯维尔。主角苏特里放弃优渥的生活，离开富裕家庭和他知识分子的过去，选择在诺克斯维尔田纳西河上的一艘破旧船屋上钓鱼卖给餐馆为生，过着自我放逐的生活。船屋靠近棚户区，周边多为怪人和罪犯：隐士、酒鬼、小偷、拾荒者、掘墓人、采蚌人和女巫。",
        "科马克·麦卡锡",
        "美国当代小说家、剧作家。已著有10部长篇小说及其他短篇作品和剧作。先后斩获普利策奖、美国国家图书奖、全美书评人协会奖等美国文学界主流奖项，多部作品被改编为影视剧。科恩兄弟根据麦卡锡小说改编的同名电影《老无所依》，获得包括最佳影片、最佳导演和最佳改编剧本在内的四项奥斯卡大奖。",
        "https://img1.doubanio.com/view/subject/l/public/s34118638.jpg")
        bookBpt.insert(book)
        book = Book(10, 10, "望江南",
        "杭家人的故事也是中国的故事，它交集了历史回忆和情感想象，既是对消逝的时间的重构，也是对文化传统现代传承的探索。这部小说为中国生活和精神的剧变与恒常提供了一种新的叙事，见证了个人史、家族史、民族史中的百年中国。",
        "王旭烽",
        "浙江农林大学教授、茶文化学科带头人，茶学与茶文化学院名誉院长，国家一级作家，第五届茅盾文学奖得主，中国国际茶文化研究会理事，浙江省茶文化研究会副会长，国家首批“四个一批”人才，国务院特殊津贴获得者，浙江省中青年科技突出成就获得者，四次获中宣部“五个一工程”奖。1980年 开始进行文学创作，迄今共发表约1000多万字作品，作品涵盖小说、散文、戏曲、话剧、随笔等。",
        "https://img2.doubanio.com/view/subject/l/public/s34101121.jpg")
        bookBpt.insert(book)
        book = Book(10, 10, "电影的理论",
        "鲁迅译介过的电影理论大师\n" +
                "风行60余年、多次再版\n" +
                "经典电影理论入门导读\n" +
                "助你轻松了解各理论名家的核心观点\n" +
                "从中获得启发，打开新视角",
        "岩崎昶",
        "日本著名电影理论家、评论家，也从事电影创作。1927年毕业于东京帝国大学（现东京大学）德国文学系，1929年参与创立日本无产阶级电影同盟。曾拍摄制作《柏油路》《五一节》等纪录片，并担任《真空地带》等故事片的制片人。作为理论家，著有《现代电影艺术》《电影与资本主义》《现代日本电影》《日本电影史》等。",
        "https://img3.doubanio.com/view/subject/l/public/s34136300.jpg")
        bookBpt.insert(book)
        book = Book(20, 20, "世界在书店中",
        "这本书不是地名索引，不是世界书店指南，\n" +
                "而是十五减二位作家对书店这种特殊空间的私人回忆。\n" +
                "对他们而言，书店是一种药或一帖处方，是一座秘密花园，\n" +
                "是抗议世界其他地方泛滥的陈词滥调、巧言令色的舞 台，\n" +
                "也是一个安全、理智的所在，\n" +
                "是一个既是灯塔也是洞穴的地方。",
        "亨利•希金斯",
        "英国著名文学评论家、历史评论家和语言学家。2005年，以《约翰逊博士的词典》一书获得英国现代语言学会的独立学者最佳作品奖。2008年，以《英语的秘密家谱》一书获得约翰•卢埃林•里斯奖和萨默塞特•毛姆奖。此外，还著有《如何读懂经典》《谁害怕简•奥斯汀》和《语言战争》等作品。",
        "https://img1.doubanio.com/view/subject/l/public/s34087077.jpg")
        bookBpt.insert(book)

    }


}