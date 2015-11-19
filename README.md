
**The app uses json for inputdata to create cardView(RecyclerView) , it also provides search functionality through list of items(Cards), Screenshot of app is in issues tab  ** 

![Screenshot](https://cloud.githubusercontent.com/assets/15728282/11260745/f2bc05d8-8e93-11e5-9bb9-f11101333244.png)
below is the json used

{
  "total_rows": 2,
  "offset": 0,
  "rows": [
    {
      "id": "530b49b778c6aa9c3f327ad0ee000c81",
      "key": "CS1280",
      "value": {
        "image": "http://codex.cs.yale.edu/avi/os-book/OS8/os8c/images/os8c-cover.jpg",
        "name": "Operating Systems Concepts",
        "type": "book",
        "link": [
          {
            "type": "buy",
            "origin": "http://www.amazon.in/Operating-System-Concepts-Wiley-Student/dp/0470233990/"
          },
          {
            "type": "download",
            "origin": "http://infoman.teikav.edu.gr/~stpapad/OS_8th_Edition.pdf"
          }
        ],
        "course": {
          "id": "CS1280",
          "name": "Introduction to OS"
        },
        "uuid": "cs1280_1"
      }
    },
    {
      "id": "530b49b778c6aa9c3f327ad0ee000070",
      "key": "CS2230",
      "value": {
        "image": "https://upload.wikimedia.org/wikipedia/en/4/41/Clrs3.jpeg",
        "name": "Introduction to Algorithms",
        "type": "book",
        "link": [
          {
            "type": "buy",
            "origin": "http://www.amazon.in/Introduction-Algorithms-Thomas-H-Cormen/dp/8120340078"
          },
          {
            "type": "download",
            "origin": "http://syedwaqarahmad.webs.com/documents/t._cormen_-_introduction_to_algorithms_3rd_edition.pdf"
          }
        ],
        "course": {
          "id": "CS2230",
          "name": "Data Structures"
        },
        "uuid": "cs2230_1"
      }
    }
  ]
}


